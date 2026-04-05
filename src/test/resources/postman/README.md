# Postman Collection for SensorGroup API

This directory contains Postman collection and environment files for testing the SensorGroup REST API.

## Files

- **SensorGroup-API.postman_collection.json** - Main API collection with all endpoints
- **SensorGroup-Environment.postman_environment.json** - Environment variables
- **README.md** - This file

## Getting Started

### 1. Import the Collection
1. Open Postman
2. Click **Import** (top-left)
3. Select **SensorGroup-API.postman_collection.json**
4. Click **Open**

### 2. Import the Environment
1. Click the **Environments** icon (left sidebar)
2. Click **Import**
3. Select **SensorGroup-Environment.postman_environment.json**
4. Click **Open**

### 3. Select Environment
1. In the top-right corner, select **SensorGroup Environment** from the environment dropdown

### 4. Start Testing
All requests are ready to use! The `{{base_url}}` variable will automatically use `http://localhost:8080`.

## Available Endpoints

### 1. **Get All Sensor Groups**
- **Method:** GET
- **URL:** `/api/sensorgroups`
- **Description:** Retrieve all sensor groups
- **Response:** Array of SensorGroupDTO objects

### 2. **Get Sensor Group by ID**
- **Method:** GET
- **URL:** `/api/sensorgroups/{id}`
- **Description:** Retrieve a single sensor group by ID
- **Response:** SensorGroupDTO object or 404 Not Found

### 3. **Create Sensor Group**
- **Method:** POST
- **URL:** `/api/sensorgroups`
- **Description:** Create a new sensor group
- **Request Body:** SensorGroupDTO with:
  - `name` (string): Group name
  - `services` (array): List of services
  - `readings` (array): List of readings with type, unit, timestamp
- **Response:** Created SensorGroupDTO with 201 status code

### 4. **Create Sensor Group - Minimal**
- **Method:** POST
- **URL:** `/api/sensorgroups`
- **Description:** Create sensor group with minimal data (alternative example)
- **Request Body:** Simplified SensorGroupDTO

### 5. **Update Sensor Group**
- **Method:** PUT
- **URL:** `/api/sensorgroups/{id}`
- **Description:** Update an existing sensor group
- **Request Body:** Updated SensorGroupDTO
- **Response:** Updated SensorGroupDTO or 404 Not Found

### 6. **Delete Sensor Group**
- **Method:** DELETE
- **URL:** `/api/sensorgroups/{id}`
- **Description:** Delete a sensor group by ID
- **Response:** 204 No Content on success or 404 Not Found

### 7. **Health Check**
- **Method:** GET
- **URL:** `/actuator/health`
- **Description:** Check application health status
- **Response:** Health status object

## Example Request/Response

### Create Sensor Group Request
```json
{
  "name": "Building A Sensors",
  "services": [
    "Temperature Monitor",
    "Humidity Sensor",
    "Air Quality"
  ],
  "readings": [
    {
      "type": "temperature",
      "unit": 22.5,
      "timestamp": "2026-04-05T10:30:00Z"
    },
    {
      "type": "humidity",
      "unit": 65.0,
      "timestamp": "2026-04-05T10:30:00Z"
    }
  ]
}
```

### Expected Response (201 Created)
```json
{
  "_id": 1,
  "name": "Building A Sensors",
  "services": [
    "Temperature Monitor",
    "Humidity Sensor",
    "Air Quality"
  ],
  "readings": [
    {
      "type": "temperature",
      "unit": 22.5,
      "timestamp": "2026-04-05T10:30:00Z"
    },
    {
      "type": "humidity",
      "unit": 65.0,
      "timestamp": "2026-04-05T10:30:00Z"
    }
  ]
}
```

## Notes

- Ensure the Spring Boot application is running on port 8080
- Modify `base_url` in the environment if running on a different port
- All timestamps should be in ISO 8601 format (YYYY-MM-DDTHH:mm:ssZ)
- The `unit` field in readings accepts numeric values (Double)
