package gov.ca.cwds.rest.resources;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.services.ServiceException;

/**
 * Standard implementation of interface {@link ISimpleResourceService} for non-CRUD services.
 * Incoming API requests delegate work to the service layer. All Non-CRUD {@link Resource} classes
 * should extend this class or nest delegate members.
 * 
 * @param <K> Key type
 * @param <Q> reQuest type
 * @param <P> resPonse type
 * 
 * @author CWDS API Team
 * @see ISimpleResourceService
 */
public abstract class SimpleResourceService<K extends Serializable, Q extends Request, P extends gov.ca.cwds.rest.api.Response>
    implements ISimpleResourceService<K, Q, P> {

  /**
   * Logger for this class.
   */
  static final Logger LOGGER = LoggerFactory.getLogger(SimpleResourceService.class);

  /**
   * Validate an incoming API request, {@link Request}, type Q.
   * 
   * @param req API request
   * @throws ConstraintViolationException if the incoming request fails validation
   */
  protected final void validateRequest(Q req) throws ConstraintViolationException {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    final Set<ConstraintViolation<Q>> violations = validator.validate(req);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  /**
   * Validate an incoming key, type K.
   * 
   * @param key serializable key, type K
   * @throws ConstraintViolationException if the incoming key fails validation
   */
  protected final void validateRequest(K key) throws ConstraintViolationException {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    final Set<ConstraintViolation<K>> violations = validator.validate(key);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  /**
   * <p>
   * Handle common exceptions uniformly and throw a wrapping {@link ServiceException}.
   * </p>
   * 
   * <p>
   * Handled Exception types:
   * </p>
   * 
   * The default method implementation handles these common Exceptions:
   * <ul>
   * <li>{@link EntityNotFoundException}</li>
   * <li>{@link PersistenceException}</li>
   * <li>{@link NullPointerException}</li>
   * <li>{@link ClassNotFoundException}</li>
   * <li>{@link NotImplementedException}</li>
   * </ul>
   * 
   * @param e Exception to handle throws ServiceException on runtime error
   */
  @CoverageIgnore
  protected void handleException(Exception e) throws ServiceException {

    if (e.getCause() instanceof ConstraintViolationException) {
      LOGGER.error("Failed validation! {}", e.getMessage(), e);

      ConstraintViolationException cve = (ConstraintViolationException) e.getCause();

      // TODO: Story #137202471:
      // Cobertura cannot handle Java 8 features released YEARS ago.

      // cve.getConstraintViolations().stream().forEach(System.out::println);
      // cve.getConstraintViolations().stream()
      // .forEach(err -> LOGGER.error("validation error: {}, invalid value: {}", err.getMessage(),
      // err.getInvalidValue()));

      for (ConstraintViolation cv : cve.getConstraintViolations()) {
        LOGGER.error("validation error: {}, invalid value: {}", cv.getMessage(),
            cv.getInvalidValue());
      }

      throw new ServiceException("Failed validation! " + e.getMessage(), cve);
    } else if (e.getCause() instanceof EntityNotFoundException) {
      LOGGER.error("NOT FOUND! {}", e.getMessage(), e);
      throw new ServiceException("NOT FOUND! " + e.getMessage(), e);
    } else if (e.getCause() instanceof PersistenceException) {
      LOGGER.error("Persistence error! {}", e.getMessage(), e);
      throw new ServiceException("Persistence error! " + e.getMessage(), e);
    } else if (e.getCause() instanceof NullPointerException) {
      LOGGER.error("NPE! {}", e.getMessage(), e);
      throw new ServiceException("NPE! " + e.getMessage(), e);
    } else if (e.getCause() instanceof ClassNotFoundException) {
      LOGGER.error("Class not found! {}", e.getMessage(), e);
      throw new ServiceException("Class not found! " + e.getMessage(), e);
    } else if (e.getCause() instanceof NotImplementedException) {
      LOGGER.error("Not implemented", e);
      throw new ServiceException("Not implemented", e);
    } else {
      LOGGER.error("Unable to handle request", e);
      throw new ServiceException(e);
    }

  }

  @CoverageIgnore
  @Override
  public P handle(Q req) throws ServiceException {
    P apiResponse = null;
    try {
      validateRequest(req);
      apiResponse = handleRequest(req);
    } catch (Exception e) {
      handleException(e);
    }
    return apiResponse;
  }

  @CoverageIgnore
  @Override
  public P find(K id) throws ServiceException {
    P apiResponse = null;
    try {
      validateRequest(id);
      apiResponse = handleFind(id);
    } catch (Exception e) {
      handleException(e);
    }
    return apiResponse;
  }

  protected abstract P handleRequest(Q req);

  protected abstract P handleFind(K id);

}
