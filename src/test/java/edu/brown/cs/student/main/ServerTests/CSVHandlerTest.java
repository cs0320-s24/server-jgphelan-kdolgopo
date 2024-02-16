package edu.brown.cs.student.main.ServerTests;

import edu.brown.cs.student.main.APIServer.CSVHandler;
import edu.brown.cs.student.main.CSVParserSearch.creators.StringCreator;
import edu.brown.cs.student.main.CSVParserSearch.parse.CSVParser;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVHandlerTest {

  private CSVHandler csvHandler;
  private Request requestMock;
  private Response responseMock;
  private static final String BASE_DIRECTORY = "/Users/kseniiadolgopolova/csv-kdolgopo/data";

  @Before
  public void setUp() {
    csvHandler = new CSVHandler();
    requestMock = new RequestMock();
  }

  @Test
  public void testLoadCSV_FileFound() {
    ((RequestMock) requestMock).setFilePath("test.csv");
    Object result = csvHandler.loadCSV(requestMock, responseMock);
    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("success", resultMap.get("result"));
  }

  @Test
  public void testLoadCSV_FileNotFound() {
    ((RequestMock) requestMock).setFilePath("nonexistent.csv");
    Object result = csvHandler.loadCSV(requestMock, responseMock);
    assertNotNull(result);
    // Assert that the result is an error response with "error_datasource"
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_datasource", resultMap.get("result"));
    assertEquals("File not found", resultMap.get("message"));
  }

  @Test
  public void testViewCSV_NullParser() throws IOException {
    Object result = csvHandler.viewCSV(requestMock, responseMock);
    assertNotNull(result);
    // Assert that the result is an error response with "error_bad_request"
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("No CSV file loaded", resultMap.get("message"));
  }

  @Test(expected = FileNotFoundException.class)
  public void testViewCSV_FileNotFound() throws IOException {
    CSVHandler.parser = new CSVParser<>(new FileReader(BASE_DIRECTORY + "/" + "nonexistent.csv"), new StringCreator(), false);
    Object result = csvHandler.viewCSV(requestMock, responseMock);
    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    // Assert that the result is an error response with "error_datasource"
    assertEquals("error_datasource", resultMap.get("result"));
    assertEquals("Error reading CSV file", resultMap.get("message"));
  }

  @Test
  public void testSearchCSV_InvalidColumnIdentifier() throws FileNotFoundException {
    ((RequestMock) requestMock).setIdentifier("nonexistent");
    // Set the parser to return empty list for headers
    CSVHandler.parser = new CSVParser<>(new FileReader(BASE_DIRECTORY + "/" + "test.csv"), new StringCreator(), false);
    // Test searchCSV method with invalid column identifier
    Object result = csvHandler.searchCSV(requestMock, responseMock);
    assertNotNull(result);

    // Assert that the result is an error response with "error_bad_request"
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid column identifier", resultMap.get("message"));
  }

  // Mock class for Request
  private static class RequestMock extends Request {
    private String filePath;
    private String identifier;

    public void setFilePath(String filePath) {
      this.filePath = filePath;
    }

    public void setIdentifier(String identifier) {
      this.identifier = identifier;
    }

    @Override
    public String queryParams(String name) {
      if ("filepath".equals(name)) {
        return filePath;
      } else if ("identifier".equals(name)) {
        return identifier;
      }
      return null;
    }
  }

}
