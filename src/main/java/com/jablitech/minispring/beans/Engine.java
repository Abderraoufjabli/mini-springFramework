package com.jablitech.minispring.beans;

import com.jablitech.minispring.annotations.Component;

@Component
public class Engine {
    public void start() {
        System.out.println("Engine started");
    }
}
