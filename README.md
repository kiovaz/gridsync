# GridSync - Energy Charging and Distribution System

## Overview

GridSync is a Java-based system for managing electric device charging stations with dynamic pricing and reporting capabilities.

## Features

- Device management (up to 50 simultaneous connections)
- Multiple charging station types with different capabilities
- Dynamic energy pricing based on station type and time
- Real-time and monthly reporting
- Billing system for charging sessions

## Requirements

- Java 11 or higher
- Maven

## How to Run

1. Clone the repository
2. Build the project:
mvn clean package

3. Run the application:

java -cp target/gridsync-1.0.0.jar br.com.kio.gridsync.App

## Running Tests

mvn test

## Project Structure

- `src/main/java`: Main application code
  - `model`: Domain classes (Devices, Stations, etc.)
  - `enums`: Enumeration types
  - `services`: Business logic
  - `utils`: Utility classes
- `src/test/java`: Unit tests

## License

MIT License
