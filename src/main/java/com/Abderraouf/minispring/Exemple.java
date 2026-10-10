package com.Abderraouf.minispring;

import com.Abderraouf.minispring.beans.VehicleService;
import com.Abderraouf.minispring.context.ApplicationContext;

public class Exemple {

    public static void main(String[] args) {

        ApplicationContext context =
                new ApplicationContext(
                        "com.Abderraouf.minispring.beans"
                );

        VehicleService vehicleService = context.getBean(VehicleService.class);
        vehicleService.saveVehicle();
    }
}