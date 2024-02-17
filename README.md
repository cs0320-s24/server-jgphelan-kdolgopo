# Server Application for Data Retrieval and Search

## Project Details

- **Project Name:** Server Application for Data Retrieval and Search
- **Project Description:** This server application provides a web API for data retrieval and search. It interfaces with two primary data sources: local CSV files and the United States Census API. The server is designed with a focus on user privacy and robust data handling, targeting technologists working to increase Internet access in underserved communities.
- **Team Members and Contributions:**
    - jgphelan (CS Login: jgphelan) - Partner
    - Kdolgopo (CS Login: kdolgopo) - Partner
- **Total Estimated Time:** 20 hours
- **Link to Repo:** [Server Repo](https://github.com/cs0320-s24/server-jgphelan-kdolgopo)

## Getting Started

## High-Level Design

### Server Architecture

The server uses a modular design, with separate handler classes for different endpoints like `loadcsv`, `viewcsv`, `searchcsv`, and `broadband`.

### Key Classes and Methods

#### `CSVHandler`

Handles CSV file operations. It includes methods for loading, viewing, and searching within CSV files.

#### `BroadbandHandler`

Interacts with the ACS API to retrieve broadband data based on state and county parameters.

#### `CacheManager`

Implements caching strategies using Google's Guava library to efficiently store and retrieve ACS API responses.

#### `CacheConfiguration`

Configures cache settings such as size and eviction policy.

### Cache Eviction Strategy

Implemented using a strategy pattern to allow flexible cache management based on different requirements.

## Runtime/Space Optimizations

- Implemented caching strategies to reduce API call overhead and improve response time.
- Used efficient data structures for storing and retrieving data.

## Errors/Bugs

### Bug 1: JSON reader cannot accept malformed JSON.

#### Reproduction Steps:
1. Navigate to BroadbandHandlerTest class
2. Run testHandle_Successful

## Checkstyle Errors

No checkstyle errors present.

## How to...

### Run Tests

To run tests, navigate to the `tests` directory and execute:

java ServerTests.java

Integration and unit tests are included to ensure the reliability of the server functionalities.

### Build and Run the Application

To start the server, run:

java Main

This command launches the server, which listens on port 4567. The server outputs minimal information such as "Server started" to indicate successful launch.

