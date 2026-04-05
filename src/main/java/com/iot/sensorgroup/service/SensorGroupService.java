package com.iot.sensorgroup.service;

import com.iot.sensorgroup.dao.SensorGroupRepository;
import com.iot.sensorgroup.dto.SensorGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorGroupService {

    @Autowired
    private SensorGroupRepository sensorGroupRepository;

    /**
     * Get all sensor groups
     */
    public List<SensorGroupDTO> getAllSensorGroups() {
        return sensorGroupRepository.findAll();
    }

    /**
     * Get sensor group by ID
     */
    public Optional<SensorGroupDTO> getSensorGroupById(Long id) {
        return sensorGroupRepository.findById(id);
    }

    /**
     * Create a new sensor group
     */
    public SensorGroupDTO createSensorGroup(SensorGroupDTO sensorGroup) {
        // Generate ID if not provided
        if (sensorGroup.getId() == null) {
            List<SensorGroupDTO> all = sensorGroupRepository.findAll();
            Long maxId = all.stream()
                    .map(SensorGroupDTO::getId)
                    .filter(id -> id != null)
                    .max(Long::compare)
                    .orElse(0L);
            sensorGroup.setId(maxId + 1);
        }
        return sensorGroupRepository.save(sensorGroup);
    }

    /**
     * Update existing sensor group
     */
    public Optional<SensorGroupDTO> updateSensorGroup(Long id, SensorGroupDTO sensorGroup) {
        Optional<SensorGroupDTO> existing = sensorGroupRepository.findById(id);
        if (existing.isPresent()) {
            sensorGroup.setId(id);
            return Optional.of(sensorGroupRepository.save(sensorGroup));
        }
        return Optional.empty();
    }

    /**
     * Delete sensor group by ID
     */
    public boolean deleteSensorGroup(Long id) {
        if (sensorGroupRepository.existsById(id)) {
            sensorGroupRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
