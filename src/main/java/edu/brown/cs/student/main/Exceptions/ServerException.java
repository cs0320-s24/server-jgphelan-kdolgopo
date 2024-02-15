package edu.brown.cs.student.main.Exceptions;

/**
 * Custom exception class for handling errors specific to the sprint project.
 */
public class ServerException extends Exception {

  /**
   * Enum to represent different types of errors that can occur.
   */
  public enum ErrorCode {
    CSV_PARSE_ERROR,
    API_REQUEST_ERROR,
    CACHE_MANAGEMENT_ERROR,
    DATA_HANDLING_ERROR,
  }


  private final ErrorCode errorCode;

  public ServerException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ServerException(String message, ErrorCode errorcode, Throwable cause) {
    super(message, cause);
    this.errorCode = errorcode;
  }

  /**
   * Gets the error code associated with this exception.
   * @return the error code.
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }
}

