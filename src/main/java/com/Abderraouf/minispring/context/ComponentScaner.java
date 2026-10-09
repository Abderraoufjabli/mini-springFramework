package com.Abderraouf.minispring.context;

import com.Abderraouf.minispring.annotations.Component;

import java.io.File;
import java.net.URL;

public class ComponentScaner {

    private final BeanContainer container;


    public ComponentScaner(BeanContainer container) {
        this.container = container;
    }


    public void scan(String packageName) {

        String path =
                packageName.replace('.', '/');

        ClassLoader classLoader =
                Thread.currentThread()
                        .getContextClassLoader();

        URL resource =
                classLoader.getResource(path);


        if (resource == null) {

            throw new IllegalArgumentException(
                    "Package " + packageName + " not found"
            );
        }


        File directory =
                new File(resource.getFile());


        File[] files =
                directory.listFiles();


        if (files == null) {

            throw new IllegalArgumentException(
                    "Package " + packageName + " is empty"
            );
        }


        for (File file : files) {

            if (!file.getName().endsWith(".class")) {
                continue;
            }


            String className =
                    packageName
                            + "."
                            + file.getName()
                            .replace(".class", "");


            try {

                Class<?> clazz =
                        Class.forName(className);


                if (isComponent(clazz)) {

                    container.registerDefinition(clazz);
                }


            } catch (ClassNotFoundException e) {

                throw new RuntimeException(
                        "Failed to load class: "
                                + className,
                        e
                );
            }
        }
    }

    private boolean isComponent(Class<?> clazz){
        if(clazz.isAnnotationPresent(Component.class)){
            return true;
        }
        for(var annotaion : clazz.getAnnotations()){
            if(annotaion.annotationType().isAnnotationPresent(Component.class)){
                return true;
            }

        }
        return false;
    }
}