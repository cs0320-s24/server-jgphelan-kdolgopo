package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.creators.Car;
import edu.brown.cs.student.main.creators.StringCreator;
import edu.brown.cs.student.main.parse.CSVParser;
import edu.brown.cs.student.main.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** Test suite for Search */
public class TestSearch {

  private CSVParser<List<String>> stringParser;
  private CSVParser<Car> carParser;
  private Search<List<String>> search;
  private CSVParser<List<String>> parser;
  private static final String BASE_DIRECTORY = "/Users/kseniiadolgopolova/csv-kdolgopo/data";

  /** Test setup: this method runs before each test method */
  @Before
  public void setUp() throws FileNotFoundException {
    stringParser =
        new CSVParser<>(new StringReader("value1,value2,value3\n"), new StringCreator(), false);
    parser =
        new CSVParser<>(
            new FileReader(BASE_DIRECTORY + "/" + "test.csv"), new StringCreator(), true);
    search = new Search<>(parser, "test.csv", "Toyota", "", -1);
  }

  /** Test with a valid value from CSV */
  @Test
  public void testValidValue() {
    List<List<String>> result = search.searchCSV();
    assertEquals(1, result.size());
    assertEquals("Toyota", result.get(0).get(0));
  }

  /** Test with an invalid value from CSV */
  @Test
  public void testInvalidValue() {
    search = new Search<>(parser, "test.csv", "Tesla", "", -1);
    List<List<String>> result = search.searchCSV();
    assertEquals(0, result.size());
  }

  /** Test with providing a String identifier */
  @Test
  public void testColumnIdentifier() {
    search = new Search<>(parser, "test.csv", "2021", "Year", -1);
    List<List<String>> result = search.searchCSV();
    assertEquals(1, result.size());
    assertEquals("2021", result.get(0).get(2));
  }

  /** Test with providing an invalid String identifier */
  @Test
  public void testInvalidColumnIdentifier() {
    search = new Search<>(parser, "test.csv", "2021", "Model", -1);
    List<List<String>> result = search.searchCSV();
    // System.out.println(result);
    assertEquals(1, result.size());
  }

  /** Test with providing an invalid String identifier and invalid value */
  @Test
  public void testInvalidColumnIdentifier1() {
    search = new Search<>(parser, "test.csv", "2000", "Model", -1);
    List<List<String>> result = search.searchCSV();
    assertEquals(0, result.size());
  }

  /** Test with providing an Int identifier */
  @Test
  public void testIntColumnIdentifier() {
    search = new Search<>(parser, "test.csv", "2021", "", 2);
    List<List<String>> result = search.searchCSV();
    assertEquals(1, result.size());
    assertEquals("2021", result.get(0).get(2));
  }

  /** Test with providing an invalid Int identifier */
  @Test
  public void testInvalidIntColumnIdentifier() {
    search = new Search<>(parser, "test.csv", "2021", "", 4);
    List<List<String>> result = search.searchCSV();
    assertEquals(0, result.size());
  }

  /** Test for case-sensitivity when searching values */
  @Test
  public void testCaseInsensitive() {
    search = new Search<>(parser, "test.csv", "tOYota", "", -1);
    List<List<String>> result = search.searchCSV();
    assertEquals(1, result.size());
    assertEquals("Toyota", result.get(0).get(0));
  }

  /** Test for a dataset with no column headers */
  @Test
  public void testNoColumnHeaders() throws FileNotFoundException {
    parser =
        new CSVParser<>(
            new FileReader(BASE_DIRECTORY + "/" + "test-no-headers.csv"),
            new StringCreator(),
            false);
    search = new Search<>(parser, "test-no-headers.csv", "Toyota", "", -1);
    List<List<String>> result = search.searchCSV();
    assertEquals(1, result.size());
    assertEquals("Toyota", result.get(0).get(0));
  }

  /** Test for providing an invalid column index */
  @Test
  public void testInvalidIndex() {
    Search<List<String>> search = new Search<>(parser, "test.csv", "2022", "", 3);
    List<List<String>> result = search.searchCSV();
    assertTrue(result.isEmpty());
  }

  /** Test for searching the value in a wrong column */
  @Test
  public void testSearchingInWrongColumn() {
    Search<List<String>> search = new Search<>(parser, "test.csv", "2022", "", 0);
    List<List<String>> result = search.searchCSV();
    assertTrue(result.isEmpty());
  }
}
