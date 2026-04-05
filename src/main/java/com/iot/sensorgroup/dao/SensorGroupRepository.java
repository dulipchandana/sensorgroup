package com.iot.sensorgroup.dao;

import com.iot.sensorgroup.dto.SensorGroupDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorGroupRepository extends MongoRepository<SensorGroupDTO, Long> {
}
