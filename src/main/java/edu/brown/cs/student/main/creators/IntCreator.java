package edu.brown.cs.student.main.creators;

import edu.brown.cs.student.main.parse.CreatorFromRow;
import edu.brown.cs.student.main.parse.FactoryFailureException;
import java.util.ArrayList;
import java.util.List;

public class IntCreator implements CreatorFromRow<List<Integer>> {
  @Override
  public List<Integer> create(List<String> row) throws FactoryFailureException {
    List<Integer> list = new ArrayList<>();

    try {
      for (String str : row) {
        list.add(Integer.parseInt(str));
      }
    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Failed to convert String to Integer", row);
    }

    return list;
  }
}
