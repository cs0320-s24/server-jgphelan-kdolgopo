package edu.brown.cs.student.main.CSVParserSearch.creators;

import edu.brown.cs.student.main.CSVParserSearch.parse.CreatorFromRow;
import edu.brown.cs.student.main.CSVParserSearch.parse.FactoryFailureException;
import java.util.List;

public class StringCreator implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
