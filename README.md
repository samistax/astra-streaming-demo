# JSON Event Processing with Spring Boot and Apache Pulsar

This project demonstrates the development of a **Spring Boot application** integrated with **Apache Pulsar**, **Vaadin**, and **DataStax Astra** to handle event-based workflows and data streams.

---

## Table of Contents
1. [Overview](#overview)
2. [Architectural Summary](#architectural-summary)
    - [Frontend and User Interface](#frontend-and-user-interface)
    - [Backend and Business Logic](#backend-and-business-logic)
    - [Schema Customization and Database Connectivity](#schema-customization-and-database-connectivity)
    - [Progressive Web App Features (PWA)](#progressive-web-app-features-pwa)
3. [Domain Model](#domain-model)
4. [Architecture Diagram](#architecture-diagram)

---

## Overview

The code demonstrates:
- Use of **Spring Boot** for microservice orchestration.
- **Apache Pulsar integration** for messaging and event processing.
- **DataStax Astra** as the cloud-based database built on Apache Cassandra.
- **Vaadin Framework** for building modern web applications with Java-based UI components.
- Progressive Web App (PWA) annotation for enhanced device compatibility.

Key features include:
- **Custom schema mapping** for events using JSON.
- **Reactive event processing** with `Flux<SampleEvent>`.
- **Custom theming and PWA configuration** for the UI component.

---

## Architectural Summary

The application is divided into tightly coupled modules for scalability and reusability. Here's an organized breakdown of the components:

### **Frontend and User Interface**
- Developed using the **Vaadin framework** for building web applications with pure Java.
- Components:
    - **MainView**: Primary UI component using `@Route` annotation.
    - Contains a **Grid** to display details of events such as event time, status code, username, transaction ID, and environment.

---

### **Backend and Business Logic**
- Powered by **Spring Boot**, utilizing:
    - Dependency management.
    - Auto-configuration.
    - Modular structure for microservices.
- Vaadin UI flows are mapped to HTTP routes automatically with `@Route`.
- **Apache Pulsar Integration**:
    - `@EnablePulsar` annotation enables event-driven messaging.
    - **PulsarService**:
        - Listens to incoming `SampleEvent` objects via `@PulsarListener`.
        - Emits the events as a `Flux<SampleEvent>` using reactive patterns.

---

### **Schema Customization and Database Connectivity**
- **SchemaResolverCustomizer**: Custom mapping ensures consistent schema with JSON-based serialization.
- **Database**:
    - Uses **DataStax Astra**, a managed Apache Cassandra database service.
    - Connectivity through `DataStaxAstraProperties` and secure connect bundles.
    - **CqlSessionBuilderCustomizer** establishes secure connections.

---

### **Progressive Web App Features (PWA)**
- Configured as a **Progressive Web App** using the `@PWA` annotation.
- Allows features like:
    - Installation on mobile and desktop devices.
    - Offline capabilities.
    - Enhanced responsiveness.

---

## Domain Model

The application manages event data through a structured domain model. Below is the representation of the `SampleEvent` domain:

### **Domain Entity: SampleEvent**
- **Namespace**: `com.samistax.dto.SampleEvent`
- **Attributes**:
    - `String transaction_id`
    - `long event_time`
    - `long cas_timestamp`
    - `String client_version`
    - `String environment`
    - `String event_log`
    - `String function`
    - `String identity`
    - `String level`
    - `String partner_transaction_id`
    - `float process_time`
    - `String product_id`
    - `String request`
    - `String request_id`
    - `String response_id`
    - `int response_size`
    - `String serial_number`
    - `String server_name`
    - `String server_version`
    - `String service`
    - `String site_code`
    - `String source`
    - `String status`
    - `int status_`

---

## Architecture Diagram

Below is a visual representation of the architecture, including **Spring Boot**, **Pulsar integration**, **Vaadin UI**, and **multi-module Maven project structure**:

![Spring Boot Architecture with Multi-Module Maven](https://user-images.githubusercontent.com/123456789/spring-boot-architecture.png)

### Explanation of Components in the Diagram:
1. **Presentation Layer**: The **Vaadin UI module** enables Java-based client-side handling and routing.
2. **Services Layer**:
    - Contains business logic.
    - Handles communication with Pulsar through **PulsarService**.
3. **Messaging Layer**:
    - Apache Pulsar mediates incoming and outgoing events through a JSON-based custom schema.
4. **Data Layer**:
    - **DataStax Astra** ensures cloud-based high-availability storage for event data.
    - Schema-managed data interaction with Cassandra.

---

This architecture ensures modularity, making the system scalable, testable, and deployable in cloud environments. Let me know if you'd like modifications or visual enhancements to the architecture!


# Maven Multi-Module Project Overview

This project is organized as a **Maven multi-module project**, which is a structure that enables modularization, reusability, and scalability. In this setup, the main project is called the **parent module** and contains various **sub-modules** (child projects), each serving a specific purpose.

---

## Why Use a Multi-Module Maven Project?

- **Code Organization**: Break down a large application into smaller, logical modules.
- **Reusability**: Sub-modules can be reused across multiple projects.
- **Dependency Management**: Centralized dependency and plugin configuration in the parent `pom.xml`.
- **Parallel Development**: Different teams or developers can work on specific modules independently.
- **Build Efficiency**: Build and test only specific modules as needed, instead of the entire project.

---

## Project Structure

Below is the high-level structure of the multi-module project:

```plaintext
parent-project
│
├── pom.xml                  # Parent Project POM (Centralized Configuration)
│
├── subproject-service       # Backend Service Layer
│   ├── pom.xml
│   ├── src/main/java
│   └── src/main/resources
│
├── subproject-messaging     # Messaging and Event Processing
│   ├── pom.xml
│   ├── src/main/java
│   └── src/main/resources
│
├── subproject-web           # Vaadin-based UI Layer (Frontend)
│   ├── pom.xml
│   ├── src/main/java
│   └── src/main/resources
│
└── subproject-database      # Database Connectivity and Data Models
    ├── pom.xml
    ├── src/main/java
    └── src/main/resources
```

---

## Modules and Their Responsibilities

### 1. **Parent Project**
- **Path**: `parent-project`
- **Purpose**:
    - Contains the `pom.xml` file with centralized dependency and plugin configuration.
    - Defines the hierarchy and modules for the entire project.
    - Ensures versioning consistency across all sub-modules.
- **Details**:
    - Manages common dependencies like Spring Boot, Apache Pulsar, Vaadin, etc.
    - Configures Maven plugins (such as packaging, testing, and building).
    - Builds all sub-modules together or individually, based on need.
    - Typically does not contain any application logic or code.

---

### 2. **Backend Service Layer**
- **Path**: `subproject-service`
- **Purpose**:
    - Implements the core backend logic for the application.
    - Handles REST APIs and other business/processing logic.
- **Features**:
    - Manages Spring Boot's backend components.
    - Configures and runs services such as `PulsarService` using reactive streams.
    - Acts as the entry point for business rules and data processing.

---

### 3. **Messaging and Event Processing**
- **Path**: `subproject-messaging`
- **Purpose**:
    - Handles asynchronous event processing and messaging integration with Apache Pulsar.
- **Features**:
    - Pulsar listeners configured for event consumption using `@PulsarListener`.
    - Maps and manages JSON schemas for messages (e.g., `SampleEvent`) in Pulsar topics.
    - Publishes and consumes events using reactive streams (`Flux<SampleEvent>`).

---

### 4. **Frontend/UI Layer**
- **Path**: `subproject-web`
- **Purpose**:
    - Implements the web-based UI using **Vaadin**.
    - Acts as the frontend layer for managing user interactions and presenting data.
- **Features**:
    - Vaadin components (e.g., `MainView`) for grids, layouts, and event visualization.
    - Configures Progressive Web App (PWA) capabilities with offline access.
    - Routes mapped automatically via Spring integration (`@Route`).

---

### 5. **Database Layer**
- **Path**: `subproject-database`
- **Purpose**:
    - Provides connectivity to the **DataStax Astra** database (or any Cassandra-based DB).
- **Features**:
    - Manages the `DataStaxAstraProperties` class for secure DB connectivity.
    - Configures a `CqlSessionBuilderCustomizer` for establishing seamless database connections.
    - Defines data models such as `SampleEvent` for serialization and storage.
    - Manages data access layer and schema management.

---

## High-Level Flow in the Project

1. **Frontend (subproject-web)**:
    - Users interact with the Vaadin-based web UI.
    - UI consumes backend services for displaying data (e.g., grids, event logs).

2. **Backend Service (subproject-service)**:
    - Processes requests from the frontend and provides necessary data.
    - Publishes and consumes messages using `subproject-messaging`.

3. **Messaging (subproject-messaging)**:
    - Acts as a bridge for event-driven architecture using Apache Pulsar.
    - Ensures reliable message transmission and processing.

4. **Database (subproject-database)**:
    - Stores and retrieves critical event information from DataStax Astra.
    - Provides a stable layer for data persistence and integrity.

---

## How to Build and Run the Multi-Module Project

1. **Build All Modules Together**:
   ```bash
   mvn clean install
   ```

2. **Build a Specific Module**:
   ```bash
   mvn clean install --projects subproject-service
   ```

3. **Run the Application**:
   Start the modules either individually or as a packaged JAR from the parent project.

4. **Run Integration Tests**:
   Execute integration tests across sub-modules with:
   ```bash
   mvn verify
   ```

---

## Benefits of This Multi-Module Setup

- **Separation of Concerns**: Each module is responsible for a single part of the system (UI, messaging, backend, etc.).
- **Reusability**: Modules like `subproject-messaging` or `subproject-database` can be reused in other projects.
- **Build Efficiency**: Allows building and testing individual modules without recompiling the entire project.
- **Scalability**: Teams can work on separate modules independently without conflicts.
- **Easier Maintenance**: Bugs or upgrades are easier to identify and address in their respective modules.

---

This setup ensures a modular, scalable, and maintainable architecture suitable for modern, distributed applications!