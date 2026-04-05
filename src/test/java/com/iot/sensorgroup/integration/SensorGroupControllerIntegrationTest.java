package com.iot.sensorgroup.integration;

import com.iot.sensorgroup.controller.SensorGroupController;
import com.iot.sensorgroup.dto.ReadingDTO;
import com.iot.sensorgroup.dto.SensorGroupDTO;
import com.iot.sensorgroup.service.SensorGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorGroupControllerIntegrationTest {

    @Mock
    private SensorGroupService sensorGroupService;

    @InjectMocks
    private SensorGroupController sensorGroupController;

    private SensorGroupDTO testSensorGroup;

    @BeforeEach
    void setUp() {
        testSensorGroup = new SensorGroupDTO();
        testSensorGroup.setId(1L);
        testSensorGroup.setName("Test Sensor Group");
        testSensorGroup.setServices(List.of("service1", "service2"));

        ReadingDTO reading = new ReadingDTO();
        reading.setType("temperature");
        reading.setUnit(25.5);
        testSensorGroup.setReadings(List.of(reading));
    }

    @Test
    void testGetAllSensorGroups_Success() {
        when(sensorGroupService.getAllSensorGroups()).thenReturn(List.of(testSensorGroup));

        ResponseEntity<?> response = sensorGroupController.getAllSensorGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllSensorGroups_Empty() {
        when(sensorGroupService.getAllSensorGroups()).thenReturn(List.of());

        ResponseEntity<?> response = sensorGroupController.getAllSensorGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetSensorGroupById_Success() {
        when(sensorGroupService.getSensorGroupById(1L)).thenReturn(Optional.of(testSensorGroup));

        ResponseEntity<?> response = sensorGroupController.getSensorGroupById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetSensorGroupById_NotFound() {
        when(sensorGroupService.getSensorGroupById(99999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = sensorGroupController.getSensorGroupById(99999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetSensorGroupById_VerifyReadings() {
        when(sensorGroupService.getSensorGroupById(1L)).thenReturn(Optional.of(testSensorGroup));

        ResponseEntity<?> response = sensorGroupController.getSensorGroupById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensorGroupDTO body = (SensorGroupDTO) response.getBody();
        assertNotNull(body.getReadings());
        assertEquals("temperature", body.getReadings().get(0).getType());
        assertEquals(25.5, body.getReadings().get(0).getUnit());
    }

    @Test
    void testCreateSensorGroup_Success() {
        SensorGroupDTO newGroup = new SensorGroupDTO();
        newGroup.setId(2L);
        newGroup.setName("New Sensor Group");
        newGroup.setServices(List.of("service3"));

        when(sensorGroupService.createSensorGroup(any(SensorGroupDTO.class))).thenReturn(newGroup);

        ResponseEntity<?> response = sensorGroupController.createSensorGroup(newGroup);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateSensorGroup_WithReadings() {
        SensorGroupDTO newGroup = new SensorGroupDTO();
        newGroup.setId(3L);
        newGroup.setName("Group with Readings");
        newGroup.setServices(List.of("tempService"));

        ReadingDTO r1 = new ReadingDTO();
        r1.setType("humidity");
        r1.setUnit(60.0);
        newGroup.setReadings(List.of(r1));

        when(sensorGroupService.createSensorGroup(any(SensorGroupDTO.class))).thenReturn(newGroup);

        ResponseEntity<?> response = sensorGroupController.createSensorGroup(newGroup);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateSensorGroup_Success() {
        SensorGroupDTO updated = new SensorGroupDTO();
        updated.setId(1L);
        updated.setName("Updated Group");
        updated.setServices(List.of("newService"));

        when(sensorGroupService.updateSensorGroup(eq(1L), any(SensorGroupDTO.class)))
                .thenReturn(Optional.of(updated));

        ResponseEntity<?> response = sensorGroupController.updateSensorGroup(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateSensorGroup_NotFound() {
        SensorGroupDTO update = new SensorGroupDTO();
        update.setName("Should Not Update");

        when(sensorGroupService.updateSensorGroup(eq(99999L), any(SensorGroupDTO.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = sensorGroupController.updateSensorGroup(99999L, update);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteSensorGroup_Success() {
        when(sensorGroupService.deleteSensorGroup(1L)).thenReturn(true);

        ResponseEntity<?> response = sensorGroupController.deleteSensorGroup(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteSensorGroup_NotFound() {
        when(sensorGroupService.deleteSensorGroup(99999L)).thenReturn(false);

        ResponseEntity<?> response = sensorGroupController.deleteSensorGroup(99999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteSensorGroup_VerifyServiceCalled() {
        when(sensorGroupService.deleteSensorGroup(1L)).thenReturn(true);

        ResponseEntity<?> response = sensorGroupController.deleteSensorGroup(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(sensorGroupService, times(1)).deleteSensorGroup(1L);
    }

    @Test
    void testGetAllSensorGroups_VerifyServiceCalled() {
        when(sensorGroupService.getAllSensorGroups()).thenReturn(List.of(testSensorGroup));

        ResponseEntity<?> response = sensorGroupController.getAllSensorGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sensorGroupService, times(1)).getAllSensorGroups();
    }

    @Test
    void testCreateSensorGroup_VerifyJson() {
        SensorGroupDTO newGroup = new SensorGroupDTO();
        newGroup.setId(4L);
        newGroup.setName("JSON Test");

        when(sensorGroupService.createSensorGroup(any(SensorGroupDTO.class))).thenReturn(newGroup);

        ResponseEntity<?> response = sensorGroupController.createSensorGroup(newGroup);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(sensorGroupService, times(1)).createSensorGroup(any(SensorGroupDTO.class));
    }

    @Test
    void testGetAllSensorGroups_MultipleItems() {
        SensorGroupDTO g2 = new SensorGroupDTO();
        g2.setId(2L);
        g2.setName("Second Group");
        g2.setServices(List.of("s3", "s4"));

        when(sensorGroupService.getAllSensorGroups()).thenReturn(List.of(testSensorGroup, g2));

        ResponseEntity<?> response = sensorGroupController.getAllSensorGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateSensorGroup_VerifyServiceCalled() {
        SensorGroupDTO newGroup = new SensorGroupDTO();
        newGroup.setId(5L);
        newGroup.setName("Service Test");

        when(sensorGroupService.createSensorGroup(any(SensorGroupDTO.class))).thenReturn(newGroup);

        ResponseEntity<?> response = sensorGroupController.createSensorGroup(newGroup);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(sensorGroupService, times(1)).createSensorGroup(any(SensorGroupDTO.class));
    }

    @Test
    void testGetSensorGroupById_VerifyCompleteData() {
        when(sensorGroupService.getSensorGroupById(1L)).thenReturn(Optional.of(testSensorGroup));

        ResponseEntity<?> response = sensorGroupController.getSensorGroupById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensorGroupDTO body = (SensorGroupDTO) response.getBody();
        assertNotNull(body.getServices());
        assertEquals("service1", body.getServices().get(0));
        assertEquals("service2", body.getServices().get(1));
    }

    @Test
    void testUpdateSensorGroup_VerifyServiceCalled() {
        SensorGroupDTO updated = new SensorGroupDTO();
        updated.setId(1L);
        updated.setName("Updated");

        when(sensorGroupService.updateSensorGroup(eq(1L), any(SensorGroupDTO.class)))
                .thenReturn(Optional.of(updated));

        ResponseEntity<?> response = sensorGroupController.updateSensorGroup(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sensorGroupService, times(1)).updateSensorGroup(eq(1L), any(SensorGroupDTO.class));
    }

    @Test
    void testDeleteSensorGroup_VerifyContent() {
        when(sensorGroupService.deleteSensorGroup(1L)).thenReturn(true);

        ResponseEntity<?> response = sensorGroupController.deleteSensorGroup(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetSensorGroupById_CheckAllFields() {
        when(sensorGroupService.getSensorGroupById(1L)).thenReturn(Optional.of(testSensorGroup));

        ResponseEntity<?> response = sensorGroupController.getSensorGroupById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensorGroupDTO body = (SensorGroupDTO) response.getBody();
        assertEquals(1L, body.getId());
        assertEquals("Test Sensor Group", body.getName());
    }

    @Test
    void testGetAllSensorGroups_VerifyResponseFormat() {
        when(sensorGroupService.getAllSensorGroups()).thenReturn(List.of());

        ResponseEntity<?> response = sensorGroupController.getAllSensorGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
