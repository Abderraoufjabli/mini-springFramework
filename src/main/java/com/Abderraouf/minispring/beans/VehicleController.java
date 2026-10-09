package com.Abderraouf.minispring.beans;

import com.Abderraouf.minispring.annotations.Controller;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public void start() {
        System.out.println("VehicleController started");
    }
    public void save(){
        vehicleService.saveVehicle();
    }

}
