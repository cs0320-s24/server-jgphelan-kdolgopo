package edu.brown.cs.student.main.ServerTests;

import edu.brown.cs.student.main.APIServer.BroadbandHandler;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BroadbandHandlerTest {

  private BroadbandHandler broadbandHandler;

  @Before
  public void setUp() {
    broadbandHandler = new BroadbandHandler();
  }

  @Test
  public void testHandle_Successful() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("California", "Los Angeles");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };
    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    System.out.println(resultMap);
    assertEquals("success", resultMap.get("result"));
    assertNotNull(resultMap.get("datetime"));
    assertEquals("California", resultMap.get("state"));
    assertEquals("Los Angeles", resultMap.get("county"));
    assertNotNull(resultMap.get("broadband_data"));
  }

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
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state or county name", resultMap.get("message"));
  }

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
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state or county name", resultMap.get("message"));
  }

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
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state or county name", resultMap.get("message"));
  }

  @Test(expected = RuntimeException.class)
  public void testHandle_ExceptionDuringFetchBroadbandData() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("California", "Los Angeles");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    // Call handle method with a request that causes an exception during broadband data fetching
    broadbandHandler.handle(request, response);
  }

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
