package gov.ca.cwds.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

public class ApiConfigurationTest {

  @Test
  public void type() throws Exception {
    assertThat(ApiConfiguration.class, notNullValue());
  }

  @Test
  public void instantiation() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    assertThat(target, notNullValue());
  }

  @Test
  public void getApplicationName_Args__() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    String actual = target.getApplicationName();
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setApplicationName_Args__String() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    String applicationName = null;
    target.setApplicationName(applicationName);
  }

  @Test
  public void getVersion_Args__() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    String actual = target.getVersion();
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setVersion_Args__String() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    String version = null;
    target.setVersion(version);
  }

  @Test
  public void getSwaggerConfiguration_Args__() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    SwaggerConfiguration actual = target.getSwaggerConfiguration();
    SwaggerConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setSwaggerConfiguration_Args__SwaggerConfiguration() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    SwaggerConfiguration swaggerConfiguration = mock(SwaggerConfiguration.class);
    target.setSwaggerConfiguration(swaggerConfiguration);
  }

  @Test
  public void getSmartyStreetsConfiguration_Args__() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    SmartyStreetsConfiguration actual = target.getSmartyStreetsConfiguration();
    SmartyStreetsConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setSmartystreetsConfiguration_Args__SmartyStreetsConfiguration() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    SmartyStreetsConfiguration smartyStreetsConfiguration = mock(SmartyStreetsConfiguration.class);
    target.setSmartystreetsConfiguration(smartyStreetsConfiguration);
  }

  @Test
  public void getShiroConfiguration_Args__() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    ShiroConfiguration actual = target.getShiroConfiguration();
    ShiroConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setShiroConfiguration_Args__ShiroConfiguration() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    ShiroConfiguration shiroConfiguration = mock(ShiroConfiguration.class);
    target.setShiroConfiguration(shiroConfiguration);
  }

  @Test
  public void getWebSecurityConfiguration_Args__() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    WebSecurityConfiguration actual = target.getWebSecurityConfiguration();
    WebSecurityConfiguration expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setWebSecurityConfiguration_Args__WebSecurityConfiguration() throws Exception {
    ApiConfiguration target = new ApiConfiguration();
    WebSecurityConfiguration webSecurityConfiguration = mock(WebSecurityConfiguration.class);
    target.setWebSecurityConfiguration(webSecurityConfiguration);
  }

}
