package edu.brown.cs.student.main.parse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CSVParser class for parsing CSV files and creating objects using a specified CreatorFromRow.
 *
 * @param <T> Type parameter representing the type of object to be created.
 */
public class CSVParser<T> {

  private CreatorFromRow<T> creator;
  private Boolean header;
  private Reader reader;
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
  private static final String BASE_DIRECTORY = "/Users/kseniiadolgopolova/csv-kdolgopo/data";

  /**
   * Constructs a CSVParser instance.
   *
   * @param reader The Reader object for reading the CSV file.
   * @param creator The CreatorFromRow implementation for creating objects from CSV rows.
   * @param header A boolean indicating whether the CSV file has a header.
   */
  public CSVParser(Reader reader, CreatorFromRow<T> creator, Boolean header) {
    this.reader = reader;
    this.creator = creator;
    this.header = header;
  }

  /**
   * Parses the CSV file, creates objects using the specified CreatorFromRow, and returns a list of
   * objects.
   *
   * @return A list of objects created from the CSV file.
   * @throws IOException If an I/O error occurs during the parsing process.
   */
  public List<T> parseCSV() throws IOException {
    List<T> parsed = new ArrayList<T>();
    // restrictAccess(filename);
    try (BufferedReader bufferedReader = new BufferedReader(this.reader)) {
      String line = bufferedReader.readLine();
      while (line != null) {
        try {
          // Split the CSV row into an array of data objects.
          String[] dataObjects = this.regexSplitCSVRow.split(line);
          List<String> columns = List.of(dataObjects);
          // Create an object from the row using the specified CreatorFromRow.
          T object = this.creator.create(columns);
          parsed.add(object);
        } catch (FactoryFailureException e) {
          System.err.println("Cannot create an object from row: " + e.getMessage());
          System.err.println("Failed row: " + e.getRow());
          System.err.println("Non-malformed rows were processed.");
        }
        line = bufferedReader.readLine();
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found or is outside of the directory");
      System.exit(1);
    } catch (IOException e) {
      System.err.println("The reader cannot be read");
      System.exit(1);
    }
    return parsed;
  }

  /**
   * Restricts access to files outside the base directory.
   *
   * @param filename The name of the file to check access for.
   */
  static void restrictAccess(String filename) {
    // Create absolute normalized paths for the file and base directory.
    Path filePath = Paths.get(BASE_DIRECTORY, filename).toAbsolutePath().normalize();
    Path baseDirectoryPath = Paths.get(BASE_DIRECTORY).toAbsolutePath().normalize();
    System.out.println("File path: " + filePath);
    System.out.println("Base directory path: " + baseDirectoryPath);
    // Check if the file path starts with the base directory path and the file exists.
    if (!filePath.startsWith(baseDirectoryPath) || !Files.exists(filePath)) {
      throw new SecurityException("File not found or is outside of the directory.");
    }
  }
}
