# REST Assured Integration Tests Conversion

## Overview
Converted the Mockito-based unit tests to **REST Assured integration tests** that test the full HTTP API stack.

## Files Created/Modified

### New Test File
- **SensorGroupControllerRestAssuredTest.java** - Full integration test suite using REST Assured

### Key Differences

| Aspect | Mockito Tests | REST Assured Tests |
|--------|---------------|-------------------|
| **Scope** | Unit tests (mocked service) | Integration tests (full stack) |
| **Database** | Not used | Real MongoDB instance required |
| **HTTP Calls** | Not made | Real HTTP requests |
| **Server** | Not started | Full Spring Boot app running |
| **Testing Level** | Controller + Mock Service | Controller + Service + Repository + DB |

## Test Coverage

### GET Endpoints
- ✅ Get all sensor groups (success, response format, multiple items)
- ✅ Get sensor group by ID (success, not found, verify complete data)
- ✅ Verify readings data

### POST Endpoints
- ✅ Create sensor group (minimal, with readings, verify JSON)
- ✅ ID generation and increment

### PUT Endpoints
- ✅ Update sensor group (success, not found, multiple fields)

### DELETE Endpoints
- ✅ Delete sensor group (success, not found, verify not found after delete)

### Health Check
- ✅ Verify application health endpoint

**Total Tests: 21**

## How to Run

### Run All REST Assured Tests
```bash
mvn test -Dtest=SensorGroupControllerRestAssuredTest
```

### Run Specific Test
```bash
mvn test -Dtest=SensorGroupControllerRestAssuredTest#testCreateSensorGroup_Success
```

### Run Both Test Suites
```bash
mvn test
```

## Prerequisites

Before running tests:

1. **MongoDB must be running**
   ```
   docker-compose -f src/main/resources/docker/docker-compose.yml up -d
   ```

2. **Spring Boot application should NOT be running**
   - Tests start their own application instance on a random port
   - The `@SpringBootTest` annotation handles this automatically

## Key Features

### 1. Real HTTP Requests
Uses REST Assured's fluent API to make actual HTTP calls:
```java
given()
    .baseUri(baseUrl)
    .contentType(ContentType.JSON)
    .body(newGroup)
.when()
    .post()
.then()
    .statusCode(201)
    .body("_id", notNullValue());
```

### 2. Automatic Server Startup
`@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` automatically:
- Starts the Spring Boot application
- Binds to a random available port
- Configures the test context

### 3. Random Port Assignment
```java
@LocalServerPort
private int port;

@BeforeEach
void setUp() {
    baseUrl = "http://localhost:" + port + "/api/sensorgroups";
}
```

### 4. Helper Methods
Reusable methods for common operations:
```java
private Integer createSensorGroupId(String name, List<String> services)
private void createSensorGroup(String name, List<String> services)
private Integer createSensorGroupWithReadingsId(SensorGroupDTO group)
```

## Test Execution Flow

1. Maven starts the test execution
2. Spring Boot test context initializes
3. Application starts on random port
4. MongoDB connection established
5. Each test:
   - Creates test data
   - Makes HTTP requests
   - Verifies responses (status, body content)
   - Cleans up (deletes created data)
6. Application shuts down after all tests complete

## Sample Test

```java
@Test
void testCreateSensorGroup_Success() {
    SensorGroupDTO newGroup = new SensorGroupDTO();
    newGroup.setName("New Sensor Group");
    newGroup.setServices(List.of("service1", "service2"));

    given()
        .baseUri(baseUrl)
        .contentType(ContentType.JSON)
        .body(newGroup)
    .when()
        .post()
    .then()
        .statusCode(201)
        .body("_id", notNullValue())
        .body("name", equalTo("New Sensor Group"));
}
```

## Assertions Used

- `statusCode(200)` - HTTP status verification
- `contentType(ContentType.JSON)` - Response content type
- `body("_id", notNullValue())` - JSON path validation
- `body("name", equalTo("Updated Name"))` - Value matching
- `body("services", hasItems(...))` - Collection assertions
- `emptyString()` - Empty response body
- `instanceOf(List.class)` - Type checking

## Data Flow

```
Test Setup
    ↓
Helper Method: createSensorGroupId()
    ↓
POST /api/sensorgroups
    ↓
Spring Boot App
    ↓
SensorGroupController
    ↓
SensorGroupService
    ↓
SensorGroupRepository
    ↓
MongoDB (actual database)
    ↓
Response with ID
    ↓
Extract ID from response
    ↓
Use ID in subsequent GET/PUT/DELETE tests
    ↓
Verify response
```

## Benefits vs Unit Tests

| Benefit | Value |
|---------|-------|
| **Full Stack Testing** | Tests actual request/response cycle |
| **Database Integration** | Verifies persistence works correctly |
| **Real HTTP** | Catches serialization issues |
| **End-to-End** | Tests complete flow from HTTP to DB |
| **Realistic** | Mirrors actual client usage |
| **API Contract** | Validates HTTP contract matches expected |

## Troubleshooting

### Test hangs or takes too long
- MongoDB might not be running
- Check Docker containers: `docker ps`
- Start MongoDB: `docker-compose -f src/main/resources/docker/docker-compose.yml up -d`

### Tests fail with connection error
- Ensure base URL is correct
- Verify Spring Boot app started on assigned port (check logs)
- Check MongoDB is accessible

### Assertion fails
- Print response body with: `.log().all()` before `.then()`
- Example: `given().baseUri(baseUrl).when().get().then().log().all().statusCode(200);`

## Migrating from Mockito to REST Assured

**Before (Mockito):**
```java
@Mock
private SensorGroupService service;

when(service.getAllSensorGroups()).thenReturn(List.of(testGroup));
```

**After (REST Assured):**
```java
given().baseUri(baseUrl)
.when().get()
.then().statusCode(200);
// Actual database query happens, no mocking
```

## Next Steps

1. Run tests: `mvn test -Dtest=SensorGroupControllerRestAssuredTest`
2. Verify all 21 tests pass
3. Use the Postman collection for manual API testing
4. Deploy to production with confidence!
