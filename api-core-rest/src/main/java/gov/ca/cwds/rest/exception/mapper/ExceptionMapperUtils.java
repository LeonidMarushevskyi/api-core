package gov.ca.cwds.rest.exception.mapper;

import static gov.ca.cwds.rest.exception.IssueDetails.BASE_MESSAGE;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;

import gov.ca.cwds.rest.exception.BaseExceptionResponse;
import gov.ca.cwds.rest.exception.IssueDetails;
import gov.ca.cwds.rest.exception.IssueType;

/**
 * General exception mapping utilities.
 * 
 * @author CWDS API Team
 */
public class ExceptionMapperUtils {

  private ExceptionMapperUtils() {
    // statics only
  }

  /**
   * Create a generic exception response
   * 
   * @param ex The exception
   * @param issueType Issue type
   * @param status Response status
   * @param incidentId Incident ID
   * @return A generic Response created from given exception
   */
  public static Response createGenericResponse(Exception ex, IssueType issueType, int status,
      String incidentId) {
    IssueDetails issueDetails = new IssueDetails();

    issueDetails.setType(issueType);
    issueDetails.setIncidentId(incidentId);
    issueDetails.setUserMessage(BASE_MESSAGE);
    issueDetails.setTechnicalMessage(ex.getMessage());

    if (ex.getCause() != null) {
      issueDetails.setTechnicalMessage(ex.getCause().getMessage());
      issueDetails.setCauseStackTrace(
          StringEscapeUtils.escapeJson(ExceptionUtils.getStackTrace(ex.getCause())));
    }
    issueDetails.setStackTrace(StringEscapeUtils.escapeJson(ExceptionUtils.getStackTrace(ex)));

    final Set<IssueDetails> detailsList = new HashSet<>();
    detailsList.add(issueDetails);
    final BaseExceptionResponse response = new BaseExceptionResponse();
    response.setIssueDetails(detailsList);

    return Response.status(status).entity(response).type(MediaType.APPLICATION_JSON).build();
  }
}
