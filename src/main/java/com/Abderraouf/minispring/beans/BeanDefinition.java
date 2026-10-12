package com.Abderraouf.minispring.beans;


import java.lang.reflect.Method;

public class BeanDefinition {

    private final Class<?> beanType;
    private final String scope;
    private final boolean primary;
    private final String name;
    private final Method factoryMethod;
    private final Object configurationInstance;

    public BeanDefinition(Class<?> beanType, String scope, boolean primary, String name) {
        this(beanType, scope, primary, name, null, null);
    }

    public BeanDefinition(Class<?> beanType, String scope, boolean primary, String name,
                          Method factoryMethod, Object configurationInstance) {
        this.beanType = beanType;
        this.scope = scope;
        this.primary = primary;
        this.name = name;
        this.factoryMethod = factoryMethod;
        this.configurationInstance = configurationInstance;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
    public String getScope() {
        return scope;
    }
    public boolean isPrimary() {
        return primary;
    }
    public String getName() {
        return name;
    }
    public Method getFactoryMethod() {
        return factoryMethod;
    }
    public Object getConfigurationInstance() {
        return configurationInstance;
    }
}


