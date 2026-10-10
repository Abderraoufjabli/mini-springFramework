package com.Abderraouf.minispring.beans;

import com.Abderraouf.minispring.annotations.Service;
import com.Abderraouf.minispring.annotations.Autowired;
import com.Abderraouf.minispring.annotations.Qualifier;

@Service
public class VehicleService {


    @Autowired
    @Qualifier("oracle")
    private VehicleRepository vehicleRepository;
    public VehicleService() {

    }

    public void start() {
        System.out.println("VehicleService started");
    }

    public void saveVehicle() {
        vehicleRepository.save();
    }
}
