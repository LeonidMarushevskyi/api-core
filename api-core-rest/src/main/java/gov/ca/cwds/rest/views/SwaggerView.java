package gov.ca.cwds.rest.views;

import com.google.common.base.Charsets;
import gov.ca.cwds.rest.SwaggerConfiguration;
import io.dropwizard.views.View;
import org.apache.commons.lang3.StringUtils;

public class SwaggerView extends View {

  SwaggerConfiguration swaggerConfiguration;
  String swaggerJsonUrl;
  String callbackUrl;

  public SwaggerView(SwaggerConfiguration swaggerConfiguration, String swaggerJsonUrl,
      String callbackUrl) {
    super(swaggerConfiguration.getTemplateName(), Charsets.UTF_8);
    this.swaggerConfiguration = swaggerConfiguration;
    this.swaggerJsonUrl = swaggerJsonUrl;
    this.callbackUrl = callbackUrl;
  }

  public String getAssetsPath() {
    return swaggerConfiguration.getAssetsPath();
  }

  public String getTitle() {
    return swaggerConfiguration.getTitle();
  }

  public String getJsonUrl() {
    return swaggerJsonUrl;
  }

  public String getLogo() {
    return swaggerConfiguration.getLogo();
  }

  public String getLoginUrl() {
    return swaggerConfiguration.getLoginUrl();
  }

  public String getLogoutUrl() {
    return swaggerConfiguration.getLogoutUrl();
  }

  public boolean getShowLoginButton() {
    return !StringUtils.isEmpty(swaggerConfiguration.getLoginUrl());
  }

  /**
   * @return the callbackUrl
   */
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public String getSpId() {
    return swaggerConfiguration.getSpId();
  }
}
