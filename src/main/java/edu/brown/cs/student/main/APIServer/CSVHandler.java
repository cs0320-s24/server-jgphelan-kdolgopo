package edu.brown.cs.student.main.APIServer;

import edu.brown.cs.student.main.CSVParserSearch.parse.CSVParser;
import edu.brown.cs.student.main.CSVParserSearch.search.Search;
import edu.brown.cs.student.main.CSVParserSearch.creators.StringCreator;
import spark.Request;
import spark.Response;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles CSV file operations such as loading, viewing, and searching.
 */
public class CSVHandler {
  private static final String BASE_DIRECTORY = "/Users/kseniiadolgopolova/csv-kdolgopo/data";
  public static CSVParser<List<String>> parser;
  private static Search<List<String>> search;

  /**
   * Loads a CSV file from the specified filepath.
   *
   * @param request  The HTTP request containing the filepath parameter.
   * @param response The HTTP response to be sent back.
   * @return A success response map if the CSV file is loaded successfully, or an error response map if the file is not found.
   */
  public static Object loadCSV(Request request, Response response) {
    String filepath = request.queryParams("filepath");
    try {
      parser = new CSVParser<>(new FileReader(filepath), new StringCreator(), false);
      return createSuccessResponse("CSV file loaded successfully");
    } catch (FileNotFoundException e) {
      return createErrorResponse("error_datasource", "File not found");
    }
  }

  /**
   * Views the contents of the loaded CSV file.
   *
   * @param request  The HTTP request.
   * @param response The HTTP response to be sent back.
   * @return A success response map containing the CSV data, or an error response map if no CSV file is loaded or an error occurs while reading the file.
   * @throws IOException If an error occurs while reading the CSV file.
   */
  public static Object viewCSV(Request request, Response response) throws IOException {
    // ensures that loadcsv was called before this request
    if (parser == null) {
      return createErrorResponse("error_bad_request", "No CSV file loaded");
    }
    try {
      List<List<String>> csvData = parser.parseCSV();
      return createSuccessResponse(csvData);
    } catch (FileNotFoundException e) {
      return createErrorResponse("error_datasource", "Error reading CSV file");
    }
  }

  /**
   * Searches for a specific value in the loaded CSV file.
   *
   * @param request  The HTTP request containing the search parameters.
   * @param response The HTTP response to be sent back.
   * @return A success response map containing the search results, or an error response map if no CSV file is loaded or an invalid column identifier is provided.
   */
  public static Object searchCSV(Request request, Response response) {
    // ensures that loadcsv was called before this request
    if (parser == null) {
      return createErrorResponse("error_bad_request", "No CSV file loaded");
    }
    String value = request.queryParams("value");
    String identifier = request.queryParams("identifier");
    int columnIndex = -1; // Default value if identifier is not provided
    if (identifier != null && !identifier.isEmpty()) {
      // Try to parse the identifier as column index
      try {
        columnIndex = Integer.parseInt(identifier);
      } catch (NumberFormatException e) {
        // If identifier is not a number, handle as column name
        List<String> headers;
        try {
          headers = parser.parseCSV().get(0);
          columnIndex = headers.indexOf(identifier);
          if (columnIndex == -1) {
            return createErrorResponse("error_bad_request", "Invalid column identifier");
          }
        } catch (IOException ex) {
          return createErrorResponse("error_datasource", "Error reading CSV file");
        }
      }
    }

    search = new Search<>(parser, "", value, "", columnIndex);
    List<List<String>> searchResults = search.searchCSV();
    return createSuccessResponse(searchResults);
  }

  private static Object createSuccessResponse(Object data) {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("result", "success");
    responseMap.put("data", data);
    return responseMap;
  }

  private static Object createErrorResponse(String errorCode, String errorMessage) {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("result", errorCode);
    responseMap.put("message", errorMessage);
    return responseMap;
  }
}
