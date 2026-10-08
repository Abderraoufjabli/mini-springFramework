package com.jablitech.minispring.beans;

import com.jablitech.minispring.annotations.Component;
import com.jablitech.minispring.annotations.PostConstruct;
import com.jablitech.minispring.annotations.PreDestroy;
import com.jablitech.minispring.annotations.Scope;



@Component
public class Vehicle {
    private Engine engine;

    public Vehicle(Engine engine) {
        this.engine = engine;
        System.out.println("Vehicle bean created");
    }

    public void start() {
        engine.start();
        System.out.println("Vehicle started");
    }
    @PostConstruct
    public void init() {
        System.out.println("Vehicle bean initialized");
    }
    @PreDestroy
    public void Destroy() {
        System.out.println("Vehicle bean destroyed");
    }
}
