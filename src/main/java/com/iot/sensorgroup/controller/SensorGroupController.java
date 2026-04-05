package com.iot.sensorgroup.controller;

import com.iot.sensorgroup.dto.SensorGroupDTO;
import com.iot.sensorgroup.service.SensorGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sensorgroups")
public class SensorGroupController {

    @Autowired
    private SensorGroupService sensorGroupService;

    /**
     * GET: Retrieve all sensor groups
     */
    @GetMapping
    public ResponseEntity<List<SensorGroupDTO>> getAllSensorGroups() {
        List<SensorGroupDTO> sensorGroups = sensorGroupService.getAllSensorGroups();
        return ResponseEntity.ok(sensorGroups);
    }

    /**
     * GET: Retrieve a sensor group by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SensorGroupDTO> getSensorGroupById(@PathVariable Long id) {
        Optional<SensorGroupDTO> sensorGroup = sensorGroupService.getSensorGroupById(id);
        return sensorGroup.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST: Create a new sensor group
     */
    @PostMapping
    public ResponseEntity<SensorGroupDTO> createSensorGroup(@RequestBody SensorGroupDTO sensorGroup) {
        SensorGroupDTO created = sensorGroupService.createSensorGroup(sensorGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT: Update an existing sensor group
     */
    @PutMapping("/{id}")
    public ResponseEntity<SensorGroupDTO> updateSensorGroup(
            @PathVariable Long id,
            @RequestBody SensorGroupDTO sensorGroup) {
        Optional<SensorGroupDTO> updated = sensorGroupService.updateSensorGroup(id, sensorGroup);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE: Delete a sensor group by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorGroup(@PathVariable Long id) {
        if (sensorGroupService.deleteSensorGroup(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

