package com.Abderraouf.minispring.beans;

import com.Abderraouf.minispring.annotations.Repository;

@Repository
public class OracleVehicleRepository implements VehicleRepository {
    @Override
    public void save() {
        System.out.println("Saving vehicle to Oracle database");
    }
}
