package edu.brown.cs.student.main.CSVParserFuncTests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import edu.brown.cs.student.main.CSVParserSearch.creators.Car;
import edu.brown.cs.student.main.CSVParserSearch.creators.CarCreator;
import edu.brown.cs.student.main.CSVParserSearch.creators.StringCreator;
import edu.brown.cs.student.main.CSVParserSearch.parse.CSVParser;
import edu.brown.cs.student.main.CSVParserSearch.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** Test class for CSVParser and related classes. */
public class TestParser {

  private CSVParser<List<String>> stringParser;
  private CSVParser<Car> carParser;
  private Search<List<String>> search;
  private CSVParser<List<String>> parser;
  private static final String BASE_DIRECTORY = "/Users/kseniiadolgopolova/csv-kdolgopo/data";

  @Before
  public void setUp() throws FileNotFoundException {
    stringParser =
        new CSVParser<>(new StringReader("value1,value2,value3\n"), new StringCreator(), false);
    parser =
        new CSVParser<>(
            new FileReader(BASE_DIRECTORY + "/" + "test.csv"), new StringCreator(), false);
    search = new Search<>(parser, "test.csv", "Toyota", "", -1);
  }

  /**
   * Test parsing CSV data with String values.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @org.junit.Test
  public void testStringParser() throws IOException {
    List<List<String>> result = stringParser.parseCSV();
    assertEquals(1, result.size());
    assertEquals(List.of("value1", "value2", "value3"), result.get(0));
  }

  /**
   * Test parsing CSV data with Car objects.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @org.junit.Test
  public void testCarParser() throws IOException {
    carParser = new CSVParser<>(new StringReader("Toyota,Camry,2022\n"), new CarCreator(), false);
    List<Car> result = carParser.parseCSV();
    carParser = new CSVParser<>(new StringReader("Toyota,Camry,2022\n"), new CarCreator(), false);
    assertEquals(1, result.size());
    assertTrue(carParser.parseCSV().get(0) instanceof Car);
  }

  /**
   * Test parsing CSV data with Car objects using FileReader.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @org.junit.Test
  public void testCarParserFileReader() throws IOException {
    carParser =
        new CSVParser<>(new FileReader(BASE_DIRECTORY + "/" + "test.csv"), new CarCreator(), true);
    List<Car> result = carParser.parseCSV();
    carParser =
        new CSVParser<>(new FileReader(BASE_DIRECTORY + "/" + "test.csv"), new CarCreator(), true);
    System.out.println(result);
    assertEquals(3, result.size());
    assertTrue(carParser.parseCSV().get(0) instanceof Car);
  }

  /**
   * Test parsing CSV data with inconsistent column count.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @org.junit.Test
  public void testInconsistentColumnCount() throws IOException {
    carParser =
        new CSVParser<>(
            new FileReader(BASE_DIRECTORY + "/" + "test_inconsistent_count.csv"),
            new CarCreator(),
            true);
    List<Car> result = carParser.parseCSV();
    carParser =
        new CSVParser<>(
            new FileReader(BASE_DIRECTORY + "/" + "test_inconsistent_count.csv"),
            new CarCreator(),
            true);
    assertEquals(3, result.size());
    assertTrue(carParser.parseCSV().get(0) instanceof Car);
  }

  /**
   * Test handling FileNotFoundException during parsing.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @org.junit.Test(expected = FileNotFoundException.class)
  public void testFileNotFoundException() throws IOException {
    CSVParser<List<String>> stringParser1 =
        new CSVParser<>(new FileReader("nonexistent.csv"), new StringCreator(), false);
    stringParser1.parseCSV();
  }

  /**
   * Test handling FactoryFailureException during parsing.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @Test
  public void testFactoryFailureException() throws IOException {
    CSVParser<Car> carParser =
        new CSVParser<>(new StringReader("Invalid,CSV,Format"), new CarCreator(), false);
    List<Car> result = carParser.parseCSV();
    assertEquals(0, result.size());
  }

  /**
   * Test parsing CSV data with headers.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @Test
  public void testHeaders() throws IOException {
    List<List<String>> result = parser.parseCSV();
    assertEquals(4, result.size()); // 4 rows with headers
    assertEquals("Name", result.get(0).get(0));
  }

  /**
   * Test parsing a CSV file with an empty row.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @Test
  public void testEmptyRow() throws IOException {
    CSVParser<List<String>> emptyRowParser =
        new CSVParser<>(new StringReader("\n"), new StringCreator(), false);
    List<List<String>> result = emptyRowParser.parseCSV();
    assertEquals(1, result.size());
  }

  /**
   * Test parsing a CSV file with multiple empty rows.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @Test
  public void testManyEmptyRows() throws IOException {
    CSVParser<List<String>> multipleEmptyRowsParser =
        new CSVParser<>(new StringReader("\n\n\n"), new StringCreator(), false);
    List<List<String>> result = multipleEmptyRowsParser.parseCSV();
    assertEquals(3, result.size());
  }

  /**
   * Test parsing a CSV file with a single value.
   *
   * @throws IOException If an I/O error occurs during parsing.
   */
  @Test
  public void testSingleValue() throws IOException {
    CSVParser<List<String>> singleValueParser =
        new CSVParser<>(new StringReader("singleValue\n"), new StringCreator(), false);
    List<List<String>> result = singleValueParser.parseCSV();
    assertEquals(1, result.size());
    assertEquals(List.of("singleValue"), result.get(0));
  }
}
