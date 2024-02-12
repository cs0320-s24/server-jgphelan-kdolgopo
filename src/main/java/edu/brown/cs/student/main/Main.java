package edu.brown.cs.student.main;

import edu.brown.cs.student.main.creators.StringCreator;
import edu.brown.cs.student.main.parse.CSVParser;
import edu.brown.cs.student.main.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  private static final String BASE_DIRECTORY = "/Users/kseniiadolgopolova/csv-kdolgopo/data";

  public static void main(String[] args) throws FileNotFoundException {
    Scanner scanner = new Scanner(System.in);
    try {
      System.out.print("Enter the CSV filename (specify subfolders if any): ");
      String filename = scanner.nextLine();

      System.out.print("Does your file have headers? Enter T for True or F for False: ");
      String bHeader = scanner.nextLine();
      boolean header = true;
      if (bHeader.equals("T")) {
        header = true;
      } else if (bHeader.equals("F")) {
        header = false;
      } else if (!bHeader.isEmpty()) {
        System.out.print("Error: provide either T or F.");
        System.exit(1);
      }

      System.out.print("Enter the search value: ");
      String value = scanner.nextLine();

      System.out.print(
          "Enter the type of identifier (i for index, h for String header if exists), or skip: ");
      String identifierType = scanner.nextLine();

      String identifier = "";
      int intIdentifier = -1;

      if (identifierType.equals("i")) {
        System.out.print("Enter the column index: ");
        intIdentifier = Integer.parseInt(scanner.nextLine());
      } else if (identifierType.equals("h") && bHeader.equals("T")) {
        System.out.print("Enter the column header: ");
        identifier = scanner.nextLine();
      } else if (!identifierType.isEmpty()) {
        System.out.print(
            "Error: your file does not have headers or you have not provided either i or h.");
        System.exit(1);
      }

      StringCreator creator = new StringCreator();
      CSVParser<List<String>> parser =
          new CSVParser<>(new FileReader(BASE_DIRECTORY + "/" + filename), creator, header);
      Search<List<String>> search =
          new Search<>(parser, filename, value, identifier, intIdentifier);

      // Retrieve and display the results of the search:
      List<List<String>> found = search.searchCSV();
      for (List<String> row : found) {
        System.out.println(row);
      }
    } catch (SecurityException e) {
      System.err.println("Security exception: " + e.getMessage());
      System.exit(1);
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
      System.exit(1);
    }
    scanner.close();
  }
}
