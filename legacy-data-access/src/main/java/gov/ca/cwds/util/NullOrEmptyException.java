package gov.ca.cwds.util;

/**
 * @author CWDS TPT-3 Team
 */
public class NullOrEmptyException extends RuntimeException {

  private static final long serialVersionUID = 6453078047667415734L;

  public NullOrEmptyException() {
  }

  public NullOrEmptyException(String message) {
    super(message);
  }
}
