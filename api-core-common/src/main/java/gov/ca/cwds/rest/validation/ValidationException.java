package gov.ca.cwds.rest.validation;

import gov.ca.cwds.rest.api.ApiException;

/**
 * Runtime exception indicating a problem when performing a validation
 * 
 * @author CWDS API Team
 * @deprecated Use BusinessValidationException
 */
@Deprecated
@SuppressWarnings("serial")

public class ValidationException extends ApiException {

  public ValidationException() {
    super();
  }

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(Throwable cause) {
    super(cause);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
