# Project Details

## Project Name
CSV Search and Parser

## Project Description
This project involves the development of a CSV parser and search utility. The parser is capable of 
reading CSV files, and the search utility can search for specific values in the CSV data.

## Team Members and Contributions
- Kseniia Dolgopolova (kdolgopo): Implemented CSV parser, search utility, and conducted testing.
- Jo (jowethaile): Debugging partner.

## Total Estimated Time
20 hours.

## Repository Link
[CSV Search and Parser Repository](https://github.com/cs0320-s24/csv-kdolgopo)

# Design Choices

## High-Level Design
The project is divided into two main components: the CSV parser (`CSVParser`) and the search utility (`Search`). 
The `CSVParser` is responsible for reading CSV files and creating objects based on the data. 
The `Search` class uses the parser to search for specific values in the CSV data.

## Class Relationships
- `CSVParser`: Reads CSV files and creates objects based on the data by using 'CreatorFromRow' and classes that implement it.
- `Search`: Uses the CSV parser to search for specific values in the CSV data.
- `CarCreator, IntCreator, StringCreator`: Example classes for testing parsing different types of objects.

## Data Structures
- `List`: Used to represent rows of CSV data.
- `String`: Used to represent individual data elements.
- `Car`: A test object to verify that parser can parse into different objects, not just List of Strings.

## Runtime/Space Optimizations
- Avoid using a dedicated restrictAccess() method to limit access to outside directories.
Instead, a path to the root directory is already defined, and accessing files whose path does not
correlate with that invokes the File Not Found exception.

# Errors/Bugs

## Bugs
- There are no known bugs in the code.

## Explanations for Checkstyle Errors
- There are no checkstyle errors in the code.

# Tests

## Testing Suites
- `TestParser`: Tests for the CSV parser.
- `TestSearch`: Tests for the search utility.

### 1. Parser Test

**Description:** This test assesses the functionality of the `CSVParser` class by parsing CSV data from various sources.

**Reproduction Steps:**
1. Create a `CSVParser` instance with a suitable `CreatorFromRow` implementation and provide CSV data.
2. Invoke `parseCSV()` on the `CSVParser` instance.
3. Inspect the contents of the returned list to ensure proper parsing.

**Test Scenarios:**
- **String Parser:** Test parsing CSV data with String values.
- **Car Parser:** Test parsing CSV data with Car objects.
- **Car Parser with File Reader:** Test parsing CSV data with Car objects using FileReader.
- **CSV with Headers:** Validate that the `CSVParser` correctly handles CSV data with headers.
- **CSV with Inconsistent Column Count:** Test the `CSVParser` behavior when encountering CSV data with inconsistent column counts.
- **FactoryFailureException Handling:** Ensure that the `CSVParser` appropriately handles the `FactoryFailureException` by testing with malformed CSV rows.
- **FileNotFoundException Handling:** Test handling FileNotFoundException during parsing to recognize files outside protected directory or nonexistent files.
- **Variations of rows:** Tests using different variations of data - empty rows, many rows, single value.

**Expected Outcome:** The `CSVParser` should accurately parse CSV data for different scenarios and handle exceptions appropriately.

### 2. SearchCSV Test

**Description:** This test evaluates the functionality of the `Search` class by searching for specific values in a CSV file.

**Reproduction Steps:**
1. Create a `Search` instance with a `CSVParser` and provide a CSV file.
2. Specify a search value, identifier type, and identifier based on the test scenario.
3. Invoke `searchCSV()` on the `Search` instance.
4. Check the contents of the returned list to ensure they match the expected search results.

**Test Scenarios:**
- **Search for Valid Value:** Verify that the `Search` correctly finds rows containing the specified value.
- **Search for Invalid Value:** Confirm that the `Search` handles the case where the value is not present in the CSV.
- **Search in Wrong Column:** Check how the `Search` behaves when searching for a value in a specific column that does not contain the value.
- **Search by Index:** Test searching for a value using the column index.
- **Search by Column Name:** Test searching for a value using the column name.
- **Search without Column Identifier:** Verify the `Search` behavior when searching for a value without providing a column identifier.
- **Case-Insensitive Search:** Test searching for a value in a different case than in the file.
- **Search without column headers:** Test searching in a file that doesn't contain column headers.

**Expected Outcome:** The `Search` class should correctly identify and return rows based on the specified search criteria for each scenario.


# How to...

## Build and Run the Program
1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE.
3. Build the project.
4. Run the `Main` class.
5. Follow the on-screen instructions to input CSV file details and perform searches.

## Run the Tests
1. Navigate to the `tests` directory.
2. Make sure to have import org.junit.Test; at the top of the testing file.
3. Click the "Run" button in the interface.
