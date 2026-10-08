package com.jablitech.minispring.beans;

import com.jablitech.minispring.annotations.Autowired;
import com.jablitech.minispring.annotations.Qualifier;
import com.jablitech.minispring.annotations.Service;

@Service
public class VehicleService {


    @Autowired
    @Qualifier("oracleVehicleRepository")
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
