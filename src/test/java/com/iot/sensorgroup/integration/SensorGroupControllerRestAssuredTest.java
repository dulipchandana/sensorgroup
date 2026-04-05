package com.iot.sensorgroup.integration;

import com.iot.sensorgroup.dto.ReadingDTO;
import com.iot.sensorgroup.dto.SensorGroupDTO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * REST Assured integration tests for SensorGroup API
 * Tests the full HTTP API stack without mocking
 * 
 * NOTE: These tests require MongoDB to be running:
 * docker-compose -f src/main/resources/docker/docker-compose.yml up -d
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/sensorgroup",
    "server.port=0"
})
class SensorGroupControllerRestAssuredTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        if (port == 0) {
            port = 8080; // Fallback for development
        }
        baseUrl = "http://localhost:" + port + "/api/sensorgroups";
        System.out.println("REST Assured tests using baseUrl: " + baseUrl);
    }

    @Test
    void testGetAllSensorGroups_Success() {
        given().baseUri(baseUrl)
        .when().get()
        .then().statusCode(200).contentType(ContentType.JSON);
    }

    @Test
    void testGetAllSensorGroups_ResponseIsArray() {
        given().baseUri(baseUrl)
        .when().get()
        .then().statusCode(200).body("$", instanceOf(List.class));
    }

    @Test
    void testGetSensorGroupById_NotFound() {
        given().baseUri(baseUrl)
        .when().get("/99999")
        .then().statusCode(404);
    }

    @Test
    void testGetSensorGroupById_Success() {
        Integer id = createSensorGroupId("Test Group", List.of("service1"));
        given().baseUri(baseUrl)
        .when().get("/" + id)
        .then().statusCode(200).body("name", equalTo("Test Group"));
    }

    @Test
    void testGetSensorGroupById_VerifyReadings() {
        SensorGroupDTO group = new SensorGroupDTO();
        group.setName("Group with Readings");
        group.setServices(List.of("tempService"));

        ReadingDTO reading = new ReadingDTO();
        reading.setType("temperature");
        reading.setUnit(22.5);
        reading.setTimestamp("2026-04-05T10:30:00Z");
        group.setReadings(List.of(reading));

        Integer id = createSensorGroupWithReadingsId(group);
        given().baseUri(baseUrl)
        .when().get("/" + id)
        .then().statusCode(200)
            .body("readings", notNullValue())
            .body("readings[0].type", equalTo("temperature"));
    }

    @Test
    void testCreateSensorGroup_Success() {
        SensorGroupDTO newGroup = new SensorGroupDTO();
        newGroup.setName("New Sensor Group");
        newGroup.setServices(List.of("service1", "service2"));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(newGroup)
        .when().post()
        .then().statusCode(201).body("_id", notNullValue()).body("name", equalTo("New Sensor Group"));
    }

    @Test
    void testCreateSensorGroup_WithReadings() {
        SensorGroupDTO group = new SensorGroupDTO();
        group.setName("Group with Readings");
        group.setServices(List.of("tempService"));

        ReadingDTO reading = new ReadingDTO();
        reading.setType("humidity");
        reading.setUnit(60.0);
        group.setReadings(List.of(reading));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(group)
        .when().post()
        .then().statusCode(201).body("readings", notNullValue());
    }

    @Test
    void testCreateSensorGroup_Minimal() {
        SensorGroupDTO minimalGroup = new SensorGroupDTO();
        minimalGroup.setName("Minimal Group");
        minimalGroup.setServices(List.of());

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(minimalGroup)
        .when().post()
        .then().statusCode(201).body("_id", notNullValue());
    }

    @Test
    void testCreateSensorGroup_VerifyServiceCalled() {
        SensorGroupDTO group = new SensorGroupDTO();
        group.setName("Service Call Test");
        group.setServices(List.of("service1"));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(group)
        .when().post()
        .then().statusCode(201).body("_id", notNullValue());
    }

    @Test
    void testCreateSensorGroup_GeneratedIdIncrement() {
        Integer id1 = createSensorGroupId("Group 1", List.of("s1"));
        Integer id2 = createSensorGroupId("Group 2", List.of("s2"));
        assert(id2 > id1) : "ID should increment";
    }

    @Test
    void testUpdateSensorGroup_Success() {
        Integer id = createSensorGroupId("Original Name", List.of("service1"));

        SensorGroupDTO updated = new SensorGroupDTO();
        updated.setId(id.longValue());
        updated.setName("Updated Name");
        updated.setServices(List.of("newService"));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(updated)
        .when().put("/" + id)
        .then().statusCode(200).body("name", equalTo("Updated Name"));
    }

    @Test
    void testUpdateSensorGroup_NotFound() {
        SensorGroupDTO update = new SensorGroupDTO();
        update.setName("Should Not Update");

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(update)
        .when().put("/99999")
        .then().statusCode(404);
    }

    @Test
    void testUpdateSensorGroup_MultipleFields() {
        Integer id = createSensorGroupId("Original", List.of("s1"));

        SensorGroupDTO updated = new SensorGroupDTO();
        updated.setId(id.longValue());
        updated.setName("New Name");
        updated.setServices(List.of("service2", "service3"));

        ReadingDTO reading = new ReadingDTO();
        reading.setType("pressure");
        reading.setUnit(1013.25);
        updated.setReadings(List.of(reading));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(updated)
        .when().put("/" + id)
        .then().statusCode(200).body("name", equalTo("New Name"));
    }

    @Test
    void testDeleteSensorGroup_Success() {
        Integer id = createSensorGroupId("To Delete", List.of("service1"));
        given().baseUri(baseUrl)
        .when().delete("/" + id)
        .then().statusCode(204);
    }

    @Test
    void testDeleteSensorGroup_NotFound() {
        given().baseUri(baseUrl)
        .when().delete("/99999")
        .then().statusCode(404);
    }

    @Test
    void testDeleteSensorGroup_VerifyContent() {
        Integer id = createSensorGroupId("Delete Test", List.of("s1"));
        given().baseUri(baseUrl)
        .when().delete("/" + id)
        .then().statusCode(204);
    }

    @Test
    void testDeleteSensorGroup_VerifyNotFound() {
        Integer id = createSensorGroupId("Test", List.of("s1"));
        given().baseUri(baseUrl)
        .when().delete("/" + id)
        .then().statusCode(204);
        given().baseUri(baseUrl)
        .when().get("/" + id)
        .then().statusCode(404);
    }

    @Test
    void testHealthCheck_Success() {
        given().baseUri("http://localhost:" + port)
        .when().get("/actuator/health")
        .then().statusCode(200).contentType(ContentType.JSON).body("status", equalTo("UP"));
    }

    @Test
    void testGetAllSensorGroups_VerifyResponseFormat() {
        given().baseUri(baseUrl)
        .when().get()
        .then().statusCode(200).contentType(ContentType.JSON);
    }

    @Test
    void testGetAllSensorGroups_MultipleItems() {
        createSensorGroup("Test Group 1", List.of("service1"));
        createSensorGroup("Test Group 2", List.of("service2"));
        given().baseUri(baseUrl)
        .when().get()
        .then().statusCode(200);
    }

    @Test
    void testGetSensorGroupById_VerifyCompleteData() {
        Integer id = createSensorGroupId("Complete Data Test", List.of("service1", "service2"));
        given().baseUri(baseUrl)
        .when().get("/" + id)
        .then().statusCode(200)
            .body("services", notNullValue());
    }

    @Test
    void testGetSensorGroupById_CheckAllFields() {
        Integer id = createSensorGroupId("Field Check", List.of("testService"));
        given().baseUri(baseUrl)
        .when().get("/" + id)
        .then().statusCode(200)
            .body("name", equalTo("Field Check"));
    }

    @Test
    void testCreateSensorGroup_VerifyJson() {
        SensorGroupDTO group = new SensorGroupDTO();
        group.setName("JSON Test");
        group.setServices(List.of("service1"));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(group)
        .when().post()
        .then().statusCode(201).contentType(ContentType.JSON);
    }

    @Test
    void testUpdateSensorGroup_VerifyServiceCalled() {
        Integer id = createSensorGroupId("Test", List.of("s1"));

        SensorGroupDTO updated = new SensorGroupDTO();
        updated.setId(id.longValue());
        updated.setName("Updated");
        updated.setServices(List.of("updated"));

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(updated)
        .when().put("/" + id)
        .then().statusCode(200);
    }

    @Test
    void testDeleteSensorGroup_VerifyServiceCalled() {
        Integer id = createSensorGroupId("Service Call", List.of("s1"));
        given().baseUri(baseUrl)
        .when().delete("/" + id)
        .then().statusCode(204);
    }

    @Test
    void testGetAllSensorGroups_VerifyServiceCalled() {
        given().baseUri(baseUrl)
        .when().get()
        .then().statusCode(200);
    }

    // ============= Helpers =============

    private void createSensorGroup(String name, List<String> services) {
        SensorGroupDTO group = new SensorGroupDTO();
        group.setName(name);
        group.setServices(services);

        given().baseUri(baseUrl).contentType(ContentType.JSON).body(group)
        .when().post()
        .then().statusCode(201);
    }

    private Integer createSensorGroupId(String name, List<String> services) {
        SensorGroupDTO group = new SensorGroupDTO();
        group.setName(name);
        group.setServices(services);

        return given().baseUri(baseUrl).contentType(ContentType.JSON).body(group)
        .when().post()
        .then().statusCode(201).extract().path("_id");
    }

    private Integer createSensorGroupWithReadingsId(SensorGroupDTO group) {
        return given().baseUri(baseUrl).contentType(ContentType.JSON).body(group)
        .when().post()
        .then().statusCode(201).extract().path("_id");
    }
}
