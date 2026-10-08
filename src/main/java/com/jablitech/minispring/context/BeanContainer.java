package com.jablitech.minispring.context;

import com.jablitech.minispring.annotations.*;
import com.jablitech.minispring.beans.BeanDefinition;

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

            Constructor<?>[] constructors = findConstructors(type);

            if (constructors.length != 1) {
                throw new RuntimeException(
                        "Class " + type.getName()
                                + " must have exactly one constructor."
                );
            }

            Constructor<?> constructor =
                    constructors[0];

            Class<?>[] parameterTypes =
                    constructor.getParameterTypes();

            Object[] parameters =
                    new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {

                parameters[i] =
                        getBean(parameterTypes[i]);
            }
           // ce ci cree l'objet en utilisant le constructeur avec les paramètres résolus
            Object bean = constructor.newInstance(parameters);
            injectFields(bean);
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

                Qualifier  qualifier = field.getAnnotation(Qualifier.class);
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

    private Constructor<?>[] findConstructors(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();

        for(Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return new Constructor<?>[]{constructor};
            }
        }
        if(constructors.length == 1) {
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
            if(method.isAnnotationPresent(com.jablitech.minispring.annotations.PreDestroy.class)){
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

    private void initializeBean(Object bean) {
        Method [] methods = bean.getClass().getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(PostConstruct.class)){
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Failed to invoke @PostConstruct method: "
                                    + method.getName()
                                    + " on bean of type: "
                                    + bean.getClass().getName(),
                            e
                    );
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