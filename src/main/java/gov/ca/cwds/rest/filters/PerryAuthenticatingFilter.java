package gov.ca.cwds.rest.filters;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import gov.ca.cwds.auth.ApiAuthenticationException;
import gov.ca.cwds.auth.PerryShiroToken;
import gov.ca.cwds.logging.AuditLogger;

public class PerryAuthenticatingFilter extends AuthenticatingFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PerryAuthenticatingFilter.class);
  private static final String PARAM_TOKEN = "token";
  private static final String HEADER_AUTHORIZATION = "Authorization";

  private AuditLogger auditLogger;

  /**
   * Constructor
   * 
   */
  public PerryAuthenticatingFilter() {}

  /**
   * Creates a token based on the request/response pair. For Perry authentication, the token is
   * pulled from the query string, with the parameter name `token`.
   *
   * @param request The servlet request
   * @param response The servlet response
   * @return the authentication token
   */
  @Override
  protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
    String tokenFromRequest = getToken(request);
    checkArgument(!Strings.isNullOrEmpty(tokenFromRequest),
        "Authorization Token must be passed as request parameter (`" + PARAM_TOKEN
            + "`) or in the (`" + HEADER_AUTHORIZATION + "`) header");

    PerryShiroToken token = new PerryShiroToken(tokenFromRequest);

    LOGGER.debug("Created a PerryShiroToken Token for: {}.", tokenFromRequest);
    return token;
  }

  /**
   * If a user is not authenticated, this method will be called, and decide if we should attempt to
   * authenticate them.
   * <p>
   * If they have a `token`, confirm the token is valid. If they don't, respond with a 401.
   * </p>
   *
   * @param request The servlet request
   * @param response The servlet response
   * @return <code>true</code> if the request should continue to be processed; false if the subclass
   *         will handle/render the response directly.
   * @throws Exception if there is an error processing the request.
   */
  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    String token = getToken(request);

    if (token == null) {
      LOGGER.debug("No token - Denying Access");
      WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
    }

    LOGGER.debug("Attempting a login.");
    return executeLogin(request, response);
  }

  private String getToken(ServletRequest request) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String token = httpServletRequest.getParameter(PARAM_TOKEN);
    if (Strings.isNullOrEmpty(token)) {
      token = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
    }
    return token;
  }

  @Override
  protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
      ServletRequest request, ServletResponse response) {
    LOGGER.debug("Invalid Token - Denying Access");
    try {
      WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not valid");
      return false;
    } catch (IOException e1) {
      LOGGER.error("Unable to send 401 for token: {}", ((PerryShiroToken) token).getToken());
      throw new ApiAuthenticationException(MessageFormat.format("Unable to send 401 for token: {0}",
          ((PerryShiroToken) token).getToken()), e1);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.shiro.web.filter.authc.AuthenticatingFilter#onLoginSuccess(org.apache.shiro.authc.
   * AuthenticationToken, org.apache.shiro.subject.Subject, javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse)
   */
  @Override
  protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
      ServletRequest request, ServletResponse response) throws Exception {
    Subject currentUser = SecurityUtils.getSubject();
    // we should never get here. we don't have sessions on APIs
    LOGGER.warn("Existing session when no sessions should be allowed, {}",
        currentUser.getPrincipal().toString());

    auditLogger.storeUserId(currentUser.getPrincipal().toString());
    return super.onLoginSuccess(token, subject, request, response);
  }

  /**
   * @param auditLogger the auditLogger to set
   */
  public void setAuditLogger(AuditLogger auditLogger) {
    this.auditLogger = auditLogger;
  }
}
