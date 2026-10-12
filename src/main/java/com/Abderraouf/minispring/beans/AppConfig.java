package com.Abderraouf.minispring.beans;

import com.Abderraouf.minispring.annotations.Bean;
import com.Abderraouf.minispring.annotations.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public MysqlVehicleRepository mysqlVehicleRepository() {
        return new MysqlVehicleRepository();
    }
}
