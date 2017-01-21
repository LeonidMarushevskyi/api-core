package gov.ca.cwds.rest;

import javax.annotation.Nullable;

import org.hibernate.validator.constraints.NotEmpty;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

public class BaseApiConfiguration extends Configuration {
  /**
   * The application name
   */
  @NotEmpty
  private String applicationName;

  /**
   * The version
   */
  @NotEmpty
  private String version;

  private SwaggerConfiguration swaggerConfiguration;

  @Nullable
  private DataSourceFactory nsDataSourceFactory;

  @Nullable
  private FlywayFactory flywayFactory;

  @Nullable
  private DataSourceFactory cmsDataSourceFactory;

  @Nullable
  private ElasticsearchConfiguration elasticsearchConfiguration;

  @Nullable
  private SmartyStreetsConfiguration smartyStreetsConfiguration;

  @Nullable
  private ShiroConfiguration shiroConfiguration;

  @JsonProperty
  public String getApplicationName() {
    return applicationName;
  }

  @JsonProperty
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * @return the version
   */
  @JsonProperty
  public String getVersion() {
    return version;
  }

  /**
   * @param version the version to set
   */
  @JsonProperty
  public void setVersion(String version) {
    this.version = version;
  }

  @JsonProperty
  public DataSourceFactory getNsDataSourceFactory() {
    return nsDataSourceFactory;
  }

  @JsonProperty
  public void setNsDataSourceFactory(DataSourceFactory dataSourceFactory) {
    this.nsDataSourceFactory = dataSourceFactory;
  }

  @JsonProperty
  public DataSourceFactory getCmsDataSourceFactory() {
    return cmsDataSourceFactory;
  }

  @JsonProperty
  public void setDataSourceFactoryLegacy(DataSourceFactory dataSourceFactory) {
    this.cmsDataSourceFactory = dataSourceFactory;
  }

  @JsonProperty
  public FlywayFactory getFlywayFactory() {
    return flywayFactory;
  }

  @JsonProperty
  public void setFlywayFactory(FlywayFactory flywayFactory) {
    this.flywayFactory = flywayFactory;
  }

  @JsonProperty(value = "swagger")
  public SwaggerConfiguration getSwaggerConfiguration() {
    return swaggerConfiguration;
  }

  @JsonProperty
  public void setSwaggerConfiguration(SwaggerConfiguration swaggerConfiguration) {
    this.swaggerConfiguration = swaggerConfiguration;
  }


  @JsonProperty(value = "elasticsearch")
  public ElasticsearchConfiguration getElasticsearchConfiguration() {
    return elasticsearchConfiguration;
  }

  @JsonProperty
  public void setElasticsearchConfiguration(ElasticsearchConfiguration elasticsearchConfiguration) {
    this.elasticsearchConfiguration = elasticsearchConfiguration;
  }

  @JsonProperty(value = "smartystreets")
  public SmartyStreetsConfiguration getSmartyStreetsConfiguration() {
    return smartyStreetsConfiguration;
  }

  @JsonProperty
  public void setSmartystreetsConfiguration(SmartyStreetsConfiguration smartyStreetsConfiguration) {
    this.smartyStreetsConfiguration = smartyStreetsConfiguration;
  }

  @JsonProperty(value = "shiro")
  public ShiroConfiguration getShiroConfiguration() {
    return shiroConfiguration;
  }

  @JsonProperty
  public void setShiroConfiguration(ShiroConfiguration shiroConfiguration) {
    this.shiroConfiguration = shiroConfiguration;
  }
}
