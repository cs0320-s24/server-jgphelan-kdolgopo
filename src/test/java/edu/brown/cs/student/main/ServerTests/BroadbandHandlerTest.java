/**
 * This class contains unit tests for the {@link BroadbandHandler} class.
 */
package edu.brown.cs.student.main.ServerTests;

import edu.brown.cs.student.main.Utilities.JsonUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.APIServer.BroadbandHandler;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;
import com.squareup.moshi.Moshi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BroadbandHandlerTest {

  private BroadbandHandler broadbandHandler;

  /**
   * Sets up the necessary objects for testing.
   */
  @Before
  public void setUp() {
    broadbandHandler = new BroadbandHandler();
  }

  /**
   * Parses a JSON string into a {@code Map<String, Object>}.
   * @param jsonString The JSON string to be parsed.
   * @return A {@code Map<String, Object>} representing the parsed JSON.
   * @throws IOException If an I/O error occurs.
   */
  private Map<String, Object> parseStringToMap(String jsonString) throws IOException {
    return JsonUtils.parseStringToMap(jsonString);
  }

  /**
   * Tests the {@link BroadbandHandler#handle(Request, Response)} method with a successful case.
   * Verifies that the handler returns the expected response when given valid state and county names.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the current thread is interrupted while waiting.
   * @throws URISyntaxException If the given string violates RFC 2396, as augmented by the above deviations.
   */
  @Test
  public void testHandle_Successful() throws IOException, InterruptedException, URISyntaxException {
    // Test setup
    Request request = createRequest("California", "Los Angeles County, California");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    // Call the handle method
    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);

    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);

    // Verify the fields in the response
    assertEquals("success", resultMap.get("result"));
    assertNotNull(resultMap.get("datetime"));
    assertEquals("California", resultMap.get("state"));
    assertEquals("Los Angeles County, California", resultMap.get("county"));

    // Deserialize the broadband_data field to a Map
    Map<String, Object> broadbandData = parseStringToMap(resultMap.get("broadband_data").toString());
    assertNotNull(broadbandData);
    assertEquals("89.9", broadbandData.get("percentage"));
    assertEquals("Los Angeles County, California", broadbandData.get("county"));
  }

  /**
   * Tests the {@link BroadbandHandler#handle(Request, Response)} method with an invalid state name.
   * Verifies that the handler returns an error response when given an invalid state name.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the current thread is interrupted while waiting.
   * @throws URISyntaxException If the given string violates RFC 2396, as augmented by the above deviations.
   */
  @Test
  public void testHandle_InvalidState() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("InvalidState", "Los Angeles");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);

    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state name", resultMap.get("message"));
  }

  /**
   * Tests the {@link BroadbandHandler#handle(Request, Response)} method with an invalid county name.
   * Verifies that the handler returns an error response when given an invalid county name.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the current thread is interrupted while waiting.
   * @throws URISyntaxException If the given string violates RFC 2396, as augmented by the above deviations.
   */
  @Test
  public void testHandle_InvalidCounty() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("California", "InvalidCounty");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);

    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid county name", resultMap.get("message"));
  }

  /**
   * Tests the {@link BroadbandHandler#handle(Request, Response)} method with both invalid state and county names.
   * Verifies that the handler returns an error response when given both invalid state and county names.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the current thread is interrupted while waiting.
   * @throws URISyntaxException If the given string violates RFC 2396, as augmented by the above deviations.
   */
  @Test
  public void testHandle_InvalidStateAndCounty() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("InvalidState", "InvalidCounty");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);

    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state name", resultMap.get("message"));
  }

  /**
   * Creates a mock request with the given state and county query parameters.
   * @param stateQueryParam The state query parameter value.
   * @param countyQueryParam The county query parameter value.
   * @return A mock {@link Request} object with the specified query parameters.
   */
  private Request createRequest(final String stateQueryParam, final String countyQueryParam) {
    return new Request() {
      @Override
      public String queryParams(String queryParam) {
        if (queryParam.equals("state")) {
          return stateQueryParam;
        } else if (queryParam.equals("county")) {
          return countyQueryParam;
        } else {
          return null;
        }
      }
    };
  }
}
