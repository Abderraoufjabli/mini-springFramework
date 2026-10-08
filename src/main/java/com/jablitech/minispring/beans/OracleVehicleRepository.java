package com.jablitech.minispring.beans;

import com.jablitech.minispring.annotations.Repository;

@Repository
public class OracleVehicleRepository implements VehicleRepository {
    @Override
    public void save() {
        System.out.println("Saving vehicle to Oracle database");
    }
}
