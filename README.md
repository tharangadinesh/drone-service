## Drones

[[_TOC_]]

---

:scroll: **START**


### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the drone**. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

---

### Task description

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case **the load is medications**.

A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has: 
- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).

Develop a service via REST API that allows clients to communicate with the drones (i.e. **dispatch controller**). The specific communicaiton with the drone is outside the scope of this task. 

The service should allow:
- registering a drone;
- loading a drone with medication items;
- checking loaded medication items for a given drone; 
- checking available drones for loading;
- check drone battery level for a given drone;

> Feel free to make assumptions for the design approach. 

---

### Requirements

While implementing your solution **please take care of the following requirements**: 

#### Functional requirements

- There is no need for UI;
- Prevent the drone from being loaded with more weight that it can carry;
- Prevent the drone from being in LOADING state if the battery level is **below 25%**;
- Introduce a periodic task to check drones battery levels and create history/audit event log for this.

---

#### Non-functional requirements

- Input/output data must be in JSON format;
- Your project must be buildable and runnable;
- Your project must have a README file with build/run/test instructions (use DB that can be run locally, e.g. in-memory, via container);
- Any data required by the application to run (e.g. reference tables, dummy data) must be preloaded in the database;
- Unit tests;
- Use a framework of your choice, but popular, up-to-date, and long-term support versions are recommended.

---

# Drone Service REST API

## Introduction
This is a REST API service for managing a fleet of drones and their loaded medications. The service provides endpoints for registering drones, loading medications, and checking drone-related information.

## Build and Run Instructions
1. Clone the repository
2. Navigate to the project directory
3. Build the project: `mvn clean package`
4. Run the application: `java -jar target/JAR_FILE.jar`

## Database Configuration
The application uses an in-memory database (H2) for demonstration purposes. The database will be created and initialized automatically upon running the application. However, no data will be preloaded for this demonstration. The data will be generated dynamically while using the API endpoints.

## API Endpoints
- POST api/v1/login : login to take a token, username = admin, password = password
- POST api/v1/drones/register: Register a new drone
- POST api/v1/drones/{serialNumber}/load: Load medication items onto a drone

- GET api/v1/drones/{serialNumber}/loaded-medications: Check loaded medication items for a given drone
- GET api/v1/drones/available-for-loading: Get available drones for loading
- GET api/v1/drones/{serialNumber}/battery-level: Check drone battery level for a given drone

The application will start, and you can access it at `http://localhost:8080`.

## Preloaded Dummy Data

The application preloads dummy data into the H2 database during startup to demonstrate various functionalities. You can find the preloaded data in the corresponding classes for `Drone` and `Medication`.

## Dependencies

The application uses the following major dependencies:

- Spring Boot 2.7.14
- Spring Security
- JSON Web Token (JWT)
- Spring Data JPA
- H2 Database (in-memory database)
- Swagger UI

## Sample Request and Response

POST /drones

Request Body:
```json
{
    "serialNumber": "DRN-00001",
    "model": "LIGHT_WEIGHT",
    "weightLimit": 500,
    "batteryCapacity": 100,
    "state": "IDLE"
}

## Swagger Integration

Swagger is integrated into the application to provide interactive API documentation. It allows developers to explore the API endpoints, view request and response models, and test the endpoints directly from the Swagger UI.

### Accessing Swagger UI

To access the Swagger UI, run the application and navigate to the following URL in your web browser:

`http://localhost:8080/swagger-ui.html`

The Swagger UI will display the available API endpoints along with their descriptions, request parameters, and response models.

##Unit Tests

To run the unit tests, execute the following command: mvn test

## License

This project is licensed under the [MIT License](LICENSE).
    
:scroll: **END** 
