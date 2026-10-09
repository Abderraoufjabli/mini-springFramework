package com.Abderraouf.minispring.context;

import com.Abderraouf.minispring.annotations.*;
import com.Abderraouf.minispring.beans.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final Map<Class<?>, BeanDefinition> beanDefinitions =
            new HashMap<>();


    public <T> void registerDefinition(Class<T> type) {

        if (!beanDefinitions.containsKey(type)) {
            Scope scope = type.getAnnotation(Scope.class);
            String scopeValue = (scope != null) ? scope.value() : "singleton";
            boolean primary = type.isAnnotationPresent(Primary.class);
            String beanName = Character.toLowerCase(type.getSimpleName().charAt(0))
                    + type.getSimpleName().substring(1);



            BeanDefinition beanDefinition =
                    new BeanDefinition(type, scopeValue, primary,beanName);

            beanDefinitions.put(type, beanDefinition);
        }
    }


    public <T> T getBean(Class<T> type) {

        Class<?> beanType;

        if (beanDefinitions.containsKey(type)) {
            beanType = type;
        } else {
            beanType = findBeanType(type);
        }



        BeanDefinition definition =
                beanDefinitions.get(beanType);

        if (definition.getScope().equals("singleton")) {

            Object bean = beans.get(beanType);

            if (bean != null) {
                return type.cast(bean);
            }
        }

        Object bean = createBean(beanType);

        if (definition.getScope().equals("singleton")) {
            beans.put(beanType, bean);
        }

        return type.cast(bean);
    }


    private Object createBean(Class<?> type) {

        try {
            // on récupère le constructeur de la classe
            // on utilise la méthode findConstructors pour trouver
            // le constructeur annoté avec @Autowired ou le constructeur par défaut

            Constructor<?>[] constructors = findConstructors(type);

            if (constructors.length != 1) {
                throw new RuntimeException(
                        "Class " + type.getName()
                                + " must have exactly one constructor."
                );
            }

            // on récupère le constructeur unique de la classe

            Constructor<?> constructor =
                    constructors[0];

            // on récupère les types des paramètres du constructeur

            Class<?>[] parameterTypes =
                    constructor.getParameterTypes();
            // on crée un tableau d'objets pour stocker les paramètres du constructeur

            Object[] parameters =
                    new Object[parameterTypes.length];
            // on résout les dépendances pour chaque paramètre du constructeur en
            // appelant la méthode getBean pour chaque type de paramètre

            for (int i = 0; i < parameterTypes.length; i++) {
                // on récupère le bean correspondant au type du paramètre

                parameters[i] =
                        getBean(parameterTypes[i]);
            }
           // ce ci cree l'objet en utilisant le constructeur avec les paramètres résolus
            Object bean = constructor.newInstance(parameters);
            // on injecte les dépendances dans les champs annotés avec @Autowired
            injectFields(bean);
            // on appelle la méthode initializeBean pour exécuter les méthodes annotées avec
            // @PostConstruct
            initializeBean(bean);

            return bean;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create bean of type: "
                            + type.getName(),
                    e
            );
        }
    }

    private void injectFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Class<?> fieldType = field.getType();

                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                Object dependency;
                if(qualifier != null) {
                    dependency = getBean(fieldType, qualifier.value());
                } else {
                    dependency = getBean(fieldType);
                }
                try {
                    field.setAccessible(true);
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(
                            "Failed to inject dependency into field: "
                                    + field.getName()
                                    + " of bean of type: "
                                    + bean.getClass().getName(),
                            e
                    );
                }
            }
        }
    }

    public <T> T getBean(Class<T> type, String beanName){
        for(BeanDefinition definition : beanDefinitions.values()){
            if(definition.getName().equals(beanName)){
                Object bean = getBean(definition.getBeanType());
                return type.cast(bean);
            }
        }
        throw new RuntimeException(
                "No bean found with name: "
                        + beanName
        );
    }

    // cree un methode findConstructors qui prend en parametre la classe et retourne le constructeur annoté avec
    // @Autowired ou le constructeur par défaut si il n'y a pas de constructeur annoté avec @Autowired

    private Constructor<?>[] findConstructors(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();

        for(Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                // si on trouve un constructeur annoté avec @Autowired, on retourne ce constructeur
                return new Constructor<?>[]{constructor};
            }
        }
        if(constructors.length == 1) {
            //tableau de constructeur contient un seul constructeur, on retourne ce constructeur
            return constructors;
        }
        throw new RuntimeException(
                "Class " + type.getName()
                        + " must have exactly one constructor or one constructor annotated " +
                        "with @Autowired."
        );

    }

    private void  destroyBean(Object bean) {
        Method [] methods = bean.getClass().getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(PreDestroy.class)){
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Failed to invoke @PreDestroy method: "
                                    + method.getName()
                                    + " on bean of type: "
                                    + bean.getClass().getName(),
                            e
                    );
                }
            }
        }
    }

    public void close() {
        for (Object bean : beans.values()) {
            destroyBean(bean);
        }
        beans.clear();
    }
    // cree un method injectMethods qui prend en parametre un objet bean et injecte les
    // dependances dans les methodes annotées avec @Autowired
    private void injectMethods(Object bean){
        Method[] methods = bean.getClass().getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(Autowired.class)){
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                for(int i= 0; i<parameterTypes.length; i++){
                    parameters[i] = getBean(parameterTypes[i]);
                }
                try {
                    method.setAccessible(true);
                    method.invoke(bean,parameters);
                }catch(Exception e){
                    throw new RuntimeException("Failed to inject dependencies into method:"
                    + method.getName()
                    + "in"
                    + bean.getClass().getName(),e);
                }
            }
        }



    }


    // cree un methode findBeanType qui prend en parametre
    // un type et retourne le type de bean correspondant dans le conteneur

    private Class<?> findBeanType(Class<?> requestedType) {
        if(beanDefinitions.containsKey(requestedType)) {
            return requestedType;
        }
        Class<?> primaryType = null;
        for(BeanDefinition definition : beanDefinitions.values()){
            Class<?> condidateType = definition.getBeanType();
            if(requestedType.isAssignableFrom(condidateType)){
                if(definition.isPrimary()){
                    if(primaryType != null){
                        throw new RuntimeException(
                                "Multiple primary beans found for type: "
                                        + requestedType.getName()
                        );
                    }
                    primaryType = condidateType;
                }
            }
        }
        if(primaryType != null){
            return primaryType;
        }
        throw new RuntimeException(
                "No bean found for type: "
                        + requestedType.getName()
        );
    }

}