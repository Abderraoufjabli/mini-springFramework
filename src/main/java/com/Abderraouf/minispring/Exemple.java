package com.Abderraouf.minispring;

import com.Abderraouf.minispring.beans.VehicleService;
import com.jablitech.minispring.beans.*;
import com.Abderraouf.minispring.context.ApplicationContext;

public class Exemple {

    public static void main(String[] args) {

        ApplicationContext context =
                new ApplicationContext(
                        "com.jablitech.minispring.beans"
                );

        VehicleService vehicleService = context.getBean(VehicleService.class);
        vehicleService.saveVehicle();
    }
}