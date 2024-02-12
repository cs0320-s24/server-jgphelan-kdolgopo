package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.parse.CSVParser;
import java.util.ArrayList;
import java.util.List;

/**
 * The Search class provides functionality to search for values in a CSV file using specified
 * criteria. Type parameter T represents the type of objects stored in the CSV file.
 */
public class Search<T> {
  private String filename;
  private final CSVParser<List<String>> parser;
  private String value;
  private int identifier;
  private String stringIdentifier;

  /**
   * Constructs a Search object.
   *
   * @param parser The CSVParser for parsing the CSV file.
   * @param filename The name of the CSV file.
   * @param value The value to search for in the CSV file.
   * @param stringIdentifier The string identifier for column filtering.
   * @param identifier The index of the column to search in.
   */
  public Search(
      CSVParser<List<String>> parser,
      String filename,
      String value,
      String stringIdentifier,
      int identifier) {
    this.filename = filename;
    this.parser = parser;
    this.value = value;
    this.identifier = identifier;
    this.stringIdentifier = stringIdentifier;
  }

  /**
   * Searches the CSV file based on specified criteria and returns matching rows.
   *
   * @return A list of rows that match the search criteria.
   */
  public List<List<String>> searchCSV() {
    List<List<String>> found = new ArrayList<>();
    try {
      List<List<String>> allRows = this.parser.parseCSV();
      int index = determineIndex(allRows.get(0));

      for (List<String> row : allRows) {
        if (matchesRow(row, index, this.value)) {
          found.add(row);
        } // if this row doesn't have the value, then go check the next row
      }
      if (found.isEmpty()) { // if no matching rows were found
        System.out.println("Given value doesn't exist");
        // System.exit(1);
      }
    } catch (Exception e) {
      System.err.println("File cannot be processed");
      // System.exit(1);
    }
    return found;
  }

  /**
   * Determines the index of the column based on the specified criteria. Only for String
   * identifiers. If user gave the index of the column, it doesn't need to be converted further.
   *
   * @param header The header row of the CSV file.
   * @return The index of the column.
   */
  private int determineIndex(List<String> header) {
    int index = -1; // Default value for no column identifier
    // user gave an index of the column:
    if (this.identifier != -1) {
      index = this.identifier;
      // user either gave a specific String header of the column:
    } else if (!this.stringIdentifier.isEmpty()) {
      index = findIndex(header);
    }
    return index;
  }

  /**
   * Finds the index of a specified column in the header.
   *
   * @param header The header row of the CSV file.
   * @return The index of the column.
   */
  private int findIndex(List<String> header) {
    // if user gives column identifier: loop through the first line of parsed list,
    // match identifier with column names, retrieve the index of that column
    for (int i = 0; i < header.size(); i++) {
      if (header.get(i).equalsIgnoreCase(this.stringIdentifier)) {
        return i;
      }
    }
    // in case identifier (header) isn't found in the first row or if
    // file doesn't have headers:
    System.err.println(
        "Invalid column identifier: " + this.stringIdentifier + ". Search on all columns.");
    return -1;
  }

  /**
   * Checks if a given row matches the specified criteria.
   *
   * @param row The row to be checked.
   * @param index The index of the column (or -1 if no specific column is selected).
   * @param value The value to search for.
   * @return True if the row matches the criteria, false otherwise.
   */
  private Boolean matchesRow(List<String> row, int index, String value) {
    // if identifier is provided and is within bounds:
    if (index >= 0 && index < row.size()) {
      if (row.get(index).toLowerCase().contains(this.value.toLowerCase())) {
        return true;
      }
      // if identifier is not provided (if index = -1), check if the row has the value
    } else if (index == -1) {
      for (String val : row) {
        if (val.toLowerCase().contains(this.value.toLowerCase())) {
          return true;
        }
      }
    }
    return false;
  }
}
