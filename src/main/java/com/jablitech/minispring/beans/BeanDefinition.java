package com.jablitech.minispring.beans;



public class BeanDefinition {

    private final Class<?> beanType;
    private final String scope;
    private final boolean primary;
    private final String name;

    public BeanDefinition(Class<?> beanType, String scope, boolean primary, String name) {
        this.beanType = beanType;
        this.scope = scope;
        this.primary = primary;
        this.name = name;
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
}

//
//public class BeanDefinition {
//
//    private Class<?> beanType;
//
//    public BeanDefinition(Class<?> beanType) {
//        this.beanType = beanType;
//    }
//    public Class<?> getBeanType() {
//        return beanType;
//    }
//}
