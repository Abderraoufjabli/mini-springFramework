package com.Abderraouf.minispring.beans;

import com.Abderraouf.minispring.annotations.Primary;
import com.Abderraouf.minispring.annotations.Repository;

@Repository
@Primary
public class MysqlVehicleRepository implements VehicleRepository {
    @Override
    public void save() {
        System.out.println("Saving vehicle to MySQL database");
    }
}
