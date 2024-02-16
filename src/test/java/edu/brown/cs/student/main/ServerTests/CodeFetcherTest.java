package edu.brown.cs.student.main.ServerTests;
import edu.brown.cs.student.main.APIServer.CodeFetcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeFetcherTest {
  @Test
  public void testGetStateCode() {
    try {
      String stateCode = CodeFetcher.getStateCode("California");
      assertNotNull(stateCode);
      assertEquals("06", stateCode);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testGetCountyCode() {
    try {
      String countyCode = CodeFetcher.getCountyCode("06", "Kings County, California");
      assertNotNull(countyCode);
      assertEquals("031", countyCode);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testGetStateCodeWithInvalidInput() {
    try {
      // Testing with a state name that doesn't exist
      String stateCode = CodeFetcher.getStateCode("Invalid State");
      assertNull(stateCode);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testGetCountyCodeWithInvalidInput() {
    try {
      // Testing with an invalid state code and county name
      String countyCode = CodeFetcher.getCountyCode("99", "Invalid County");
      assertNull(countyCode);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
}
