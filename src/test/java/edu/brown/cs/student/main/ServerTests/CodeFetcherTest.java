/**
 * This class contains unit tests for the {@link CodeFetcher} class.
 */
package edu.brown.cs.student.main.ServerTests;

import edu.brown.cs.student.main.APIServer.CodeFetcher;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeFetcherTest {

  /**
   * Tests the {@link CodeFetcher#getStateCode(String)} method with a valid state name.
   */
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

  /**
   * Tests the {@link CodeFetcher#getCountyCode(String, String)} method with valid inputs.
   */
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

  /**
   * Tests the {@link CodeFetcher#getStateCode(String)} method with an invalid state name.
   */
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

  /**
   * Tests the {@link CodeFetcher#getCountyCode(String, String)} method with invalid inputs.
   * @throws IOException If an I/O error occurs.
   * @throws URISyntaxException If a URI syntax error occurs.
   * @throws InterruptedException If the operation is interrupted.
   */
  @Test(expected = NullPointerException.class)
  public void testGetCountyCodeWithInvalidInput()
      throws IOException, URISyntaxException, InterruptedException {
    // Testing with an invalid state code and county name
    String countyCode = CodeFetcher.getCountyCode("99", "Invalid County");
    assertNull(countyCode);
  }

}
