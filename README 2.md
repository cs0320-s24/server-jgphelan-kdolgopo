# Server Application for Data Retrieval and Search

## Overview

This server application provides a web API for data retrieval and search. It interfaces with two primary data sources: local CSV files and the United States Census API. The server is designed with a focus on user privacy and robust data handling, targeting technologists working to increase Internet access in underserved communities.

## Getting Started

### Prerequisites

- Java (Version specified)
- IntelliJ IDEA or any Java IDE
- Access to the American Community Survey (ACS) API (API key needed)

### Setting Up

Clone the repository from this repo. Import the project into IntelliJ or your preferred IDE. Ensure all dependencies listed in the `pom.xml` file are correctly installed.

### Building the Application

Navigate to the root directory of the project and run the following command to build the application:

java Main.java


### Running the Application

To start the server, run:

java Main


This command launches the server, which listens on port [specify port]. The server outputs minimal information such as "Server started" to indicate successful launch.

## Testing

To run tests, navigate to the `tests` directory and execute:


java ServerTests.java


Integration and unit tests are included to ensure the reliability of the server functionalities.

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


## Authors

- jgphelan - *Partner*
- Kdolgopo - *Partner*
