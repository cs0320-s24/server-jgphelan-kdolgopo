package edu.brown.cs.student.main.ServerTests;
import edu.brown.cs.student.main.APIServer.BroadbandHandler;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

public class BroadbandHandlerTest {

  private BroadbandHandler broadbandHandler;
  private RequestMock requestMock;
  private ResponseMock responseMock;

  @Before
  public void setUp() {
    broadbandHandler = new BroadbandHandler();
    requestMock = new RequestMock();
    responseMock = new ResponseMock();
  }

  @Test
  public void testHandle_Successful() throws IOException, InterruptedException, URISyntaxException {
    requestMock.setStateQueryParam("California");
    requestMock.setCountyQueryParam("Los Angeles");

    Object result = broadbandHandler.handle(requestMock, responseMock);

    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("success", resultMap.get("result"));
    assertNotNull(resultMap.get("datetime"));
    assertEquals("California", resultMap.get("state"));
    assertEquals("Los Angeles", resultMap.get("county"));
    assertNotNull(resultMap.get("broadband_data"));
  }

  @Test
  public void testHandle_InvalidState() throws IOException, InterruptedException, URISyntaxException {
    requestMock.setStateQueryParam("InvalidState");
    requestMock.setCountyQueryParam("Los Angeles");

    Object result = broadbandHandler.handle(requestMock, responseMock);

    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state or county name", resultMap.get("message"));
  }

  @Test
  public void testHandle_InvalidCounty() throws IOException, InterruptedException, URISyntaxException {
    requestMock.setStateQueryParam("California");
    requestMock.setCountyQueryParam("InvalidCounty");

    Object result = broadbandHandler.handle(requestMock, responseMock);

    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state or county name", resultMap.get("message"));
  }

  @Test
  public void testHandle_InvalidStateAndCounty() throws IOException, InterruptedException, URISyntaxException {
    requestMock.setStateQueryParam("InvalidState");
    requestMock.setCountyQueryParam("InvalidCounty");

    Object result = broadbandHandler.handle(requestMock, responseMock);

    assertNotNull(result);
    Map<String, Object> resultMap = (Map<String, Object>) result;
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state or county name", resultMap.get("message"));
  }

  @Test(expected = RuntimeException.class)
  public void testHandle_ExceptionDuringFetchBroadbandData() throws IOException, InterruptedException, URISyntaxException {
    requestMock.setStateQueryParam("California");
    requestMock.setCountyQueryParam("Los Angeles");

    //responseMock.setStatusCode(500);

    broadbandHandler.handle(requestMock, responseMock);
  }

  public class RequestMock extends Request {

    private String stateQueryParam;
    private String countyQueryParam;

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

    public void setStateQueryParam(String stateQueryParam) {
      this.stateQueryParam = stateQueryParam;
    }

    public void setCountyQueryParam(String countyQueryParam) {
      this.countyQueryParam = countyQueryParam;
    }
  }

  public class ResponseMock extends Response {
  }
}
