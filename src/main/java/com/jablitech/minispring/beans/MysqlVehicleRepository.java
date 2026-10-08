package com.jablitech.minispring.beans;

import com.jablitech.minispring.annotations.Primary;
import com.jablitech.minispring.annotations.Repository;

@Repository
@Primary
public class MysqlVehicleRepository implements VehicleRepository {
    @Override
    public void save() {
        System.out.println("Saving vehicle to MySQL database");
    }
}
