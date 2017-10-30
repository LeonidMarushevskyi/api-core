package gov.ca.cwds.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Read JSON configuration for Swagger settings.
 * 
 * @author CWDS API Team
 */
public class SwaggerConfiguration {

  @JsonProperty
  @NotEmpty
  private String templateName;

  @JsonProperty
  @NotEmpty
  private String assetsPath;

  @JsonProperty
  @NotEmpty
  private String resourcePackage;

  @JsonProperty
  @NotEmpty
  private String title;

  @JsonProperty
  @NotEmpty
  private String description;

  @JsonProperty
  @NotEmpty
  private String jsonUrl;

  @JsonProperty
  @NotEmpty
  private String callbackUrl;

  @JsonProperty
  @NotEmpty
  private String logo;

  @JsonProperty
  private String loginUrl;

  @JsonProperty
  private String tokenUrl;

  @JsonProperty
  private String logoutUrl;

  @JsonProperty
  private boolean showSwagger = false;

  @JsonProperty
  private String spId;

  /**
   * @return the templateName
   */
  public String getTemplateName() {
    return templateName;
  }

  /**
   * @return the assetsPath
   */
  public String getAssetsPath() {
    return assetsPath;
  }

  /**
   * @return the resourcePackage
   */
  public String getResourcePackage() {
    return resourcePackage;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the jsonUrl
   */
  public String getJsonUrl() {
    return jsonUrl;
  }

  /**
   * @return the callbackUrl
   */
  public String getCallbackUrl() {
    return callbackUrl;
  }

  /**
   * @return the logo
   */
  public String getLogo() {
    return logo;
  }

  /**
   * @return the loginUrl
   */
  public String getLoginUrl() {
    return loginUrl;
  }

  /**
   * @return the logoutUrl
   */
  public String getLogoutUrl() {
    return logoutUrl;
  }

  /**
   * @return the showSwagger
   */
  public boolean isShowSwagger() {
    return showSwagger;
  }

  /**
   * @return the token
   */
  public String getTokenUrl() {
    return tokenUrl;
  }

  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  @SuppressWarnings("javadoc")
  public String getSpId() {
    return spId;
  }
}
