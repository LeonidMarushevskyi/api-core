package gov.ca.cwds.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

public class BaseApiConfigurationTest {

  @Test
  public void type() throws Exception {
    assertThat(BaseApiConfiguration.class, notNullValue());
  }

  @Test
  public void instantiation() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    assertThat(target, notNullValue());
  }

  @Test
  public void getApplicationName_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    String actual = target.getApplicationName();
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setApplicationName_Args__String() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    String applicationName = null;
    target.setApplicationName(applicationName);
  }

  @Test
  public void getVersion_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    String actual = target.getVersion();
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setVersion_Args__String() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    String version = null;
    target.setVersion(version);
  }

  @Test
  public void getNsDataSourceFactory_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    DataSourceFactory actual = target.getNsDataSourceFactory();
    DataSourceFactory expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setNsDataSourceFactory_Args__DataSourceFactory() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
    target.setNsDataSourceFactory(dataSourceFactory);
  }

  @Test
  public void getCmsDataSourceFactory_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    DataSourceFactory actual = target.getCmsDataSourceFactory();
    DataSourceFactory expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setDataSourceFactoryLegacy_Args__DataSourceFactory() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
    target.setDataSourceFactoryLegacy(dataSourceFactory);
  }

  @Test
  public void getFlywayFactory_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    FlywayFactory actual = target.getFlywayFactory();
    FlywayFactory expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setFlywayFactory_Args__FlywayFactory() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    FlywayFactory flywayFactory = mock(FlywayFactory.class);
    target.setFlywayFactory(flywayFactory);
  }

  @Test
  public void getSwaggerConfiguration_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    SwaggerConfiguration actual = target.getSwaggerConfiguration();
    SwaggerConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setSwaggerConfiguration_Args__SwaggerConfiguration() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    SwaggerConfiguration swaggerConfiguration = mock(SwaggerConfiguration.class);
    target.setSwaggerConfiguration(swaggerConfiguration);
  }

  @Test
  public void getElasticsearchConfiguration_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    Map<String, ElasticsearchConfiguration> actual = target.getElasticsearchConfigurations();
    assertThat(actual, is(notNullValue()));
  }

  @Test
  public void setElasticsearchConfiguration_Args__ElasticsearchConfiguration() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    ElasticsearchConfiguration elasticsearchConfiguration = mock(ElasticsearchConfiguration.class);
    Map<String, ElasticsearchConfiguration> esConfigs = new HashMap<>();
    esConfigs.put("some-key", elasticsearchConfiguration);
    target.setElasticsearchConfigurations(esConfigs);
  }

  @Test
  public void getSmartyStreetsConfiguration_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    SmartyStreetsConfiguration actual = target.getSmartyStreetsConfiguration();
    SmartyStreetsConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void getTriggerTablesConfiguration_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    TriggerTablesConfiguration actual = target.getTriggerTablesConfiguration();
    TriggerTablesConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setSmartystreetsConfiguration_Args__SmartyStreetsConfiguration() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    SmartyStreetsConfiguration smartyStreetsConfiguration = mock(SmartyStreetsConfiguration.class);
    target.setSmartystreetsConfiguration(smartyStreetsConfiguration);
  }

  @Test
  public void getShiroConfiguration_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    ShiroConfiguration actual = target.getShiroConfiguration();
    ShiroConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setShiroConfiguration_Args__ShiroConfiguration() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    ShiroConfiguration shiroConfiguration = mock(ShiroConfiguration.class);
    target.setShiroConfiguration(shiroConfiguration);
  }

  @Test
  public void getWebSecurityConfiguration_Args__() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    WebSecurityConfiguration actual = target.getWebSecurityConfiguration();
    WebSecurityConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setWebSecurityConfiguration_Args__WebSecurityConfiguration() throws Exception {
    BaseApiConfiguration target = new BaseApiConfiguration();
    WebSecurityConfiguration webSecurityConfiguration = mock(WebSecurityConfiguration.class);
    target.setWebSecurityConfiguration(webSecurityConfiguration);
  }

}
