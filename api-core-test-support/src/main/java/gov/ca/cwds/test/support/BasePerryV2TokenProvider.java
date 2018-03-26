package gov.ca.cwds.test.support;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;

/**
 * @author CWDS TPT-2 Team
 */
public abstract class BasePerryV2TokenProvider<T extends AuthParams> implements
    TokenProvider<T> {

  private static final String PATH_PERRY_AUTHN_LOGIN = "/perry/authn/login";
  private static final String PATH_PERRY_AUTHN_TOKEN = "/perry/authn/token";
  private static final String CALLBACK = "callback";
  private static final String ACCESS_CODE = "accessCode";

  private String perryUrl;
  private String loginFormTargetUrl;

  private Client client;

  BasePerryV2TokenProvider(Client client, String perryUrl, String loginFormTargetUrl) {

    this.client = client;
    this.perryUrl = perryUrl;
    this.loginFormTargetUrl = loginFormTargetUrl;
  }

  abstract Form prepareLoginForm(T config);

  @Override
  public String doGetToken(T params) {
    final Map<String, NewCookie> cookies = postLoginFormAndGetJSessionIdCookie(params);
    final String accessCode = getAccessCodeFromPerry(cookies);
    return getTokenFromPerry(accessCode);
  }

  private Map<String, NewCookie> postLoginFormAndGetJSessionIdCookie(T params) {
    Form loginForm = prepareLoginForm(params);
    final Entity<Form> entity = Entity.form(loginForm);
    final Response response = client.target(loginFormTargetUrl)
        .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
        .request()
        .header(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
        .post(entity);
    return response.getCookies();
  }

  private String getAccessCodeFromPerry(Map<String, NewCookie> cookies) {
    final Builder request = client.target(perryUrl)
        .path(PATH_PERRY_AUTHN_LOGIN)
        .queryParam(CALLBACK, "/")
        .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
        .request();
    cookies.forEach((key, value) -> request.cookie(key, value.getValue()));
    final Response response = request.get();
    return parseAccessCode(response.getLocation());
  }

  private String parseAccessCode(URI uri) {
    return Arrays.stream(uri.getQuery()
        .split("&"))
        .filter(paramStr -> paramStr.startsWith(ACCESS_CODE))
        .findFirst()
        .map(s -> s.substring(s.indexOf("=") + 1))
        .orElse(null);
  }

  private String getTokenFromPerry(String accessCode) {
    final Response response = client.target(perryUrl)
        .path(PATH_PERRY_AUTHN_TOKEN)
        .queryParam(ACCESS_CODE, accessCode)
        .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
        .request()
        .get();

    return response.readEntity(String.class);
  }


}
