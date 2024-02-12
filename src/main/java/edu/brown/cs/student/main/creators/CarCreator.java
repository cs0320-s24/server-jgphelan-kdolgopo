package edu.brown.cs.student.main.creators;

import edu.brown.cs.student.main.parse.CreatorFromRow;
import edu.brown.cs.student.main.parse.FactoryFailureException;
import java.util.List;

public class CarCreator implements CreatorFromRow<Car> {

  @Override
  public Car create(List<String> row) throws FactoryFailureException {
    try {
      String make = row.get(0);
      String model = row.get(1);
      int year = Integer.parseInt(row.get(2));
      return new Car(make, model, year);

    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      throw new FactoryFailureException("Failed to create Car from row", row);
    }
  }
}
