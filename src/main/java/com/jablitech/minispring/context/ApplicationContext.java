package com.jablitech.minispring.context;

public class ApplicationContext {

    private final BeanContainer beanContainer;


    public ApplicationContext(String basePackage) {

        beanContainer =
                new BeanContainer();

        ComponentScaner scaner =
                new ComponentScaner(beanContainer);

        scaner.scan(basePackage);
    }


    public <T> T getBean(Class<T> type) {

        return beanContainer.getBean(type);
    }

    public void close() {
        beanContainer.close();
    }
}