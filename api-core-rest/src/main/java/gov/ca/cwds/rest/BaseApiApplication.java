package gov.ca.cwds.rest;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import gov.ca.cwds.rest.resources.TokenResource;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.secnod.dropwizard.shiro.ShiroBundle;
import org.secnod.dropwizard.shiro.ShiroConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.hubspot.dropwizard.guice.GuiceBundle;

import gov.ca.cwds.ObjectMapperUtils;
import gov.ca.cwds.logging.LoggingContext;
import gov.ca.cwds.rest.exception.CustomExceptionMapperBinder;
import gov.ca.cwds.rest.exception.mapper.ApiSecurityExceptionMapper;
import gov.ca.cwds.rest.exception.mapper.BusinessValidationExceptionMapper;
import gov.ca.cwds.rest.exception.mapper.DaoExceptionMapper;
import gov.ca.cwds.rest.exception.mapper.ExpectedExceptionMapperImpl;
import gov.ca.cwds.rest.exception.mapper.ReferentialIntegrityExceptionMapper;
import gov.ca.cwds.rest.exception.mapper.ServiceExceptionMapper;
import gov.ca.cwds.rest.exception.mapper.UnexpectedExceptionMapperImpl;
import gov.ca.cwds.rest.exception.mapper.ValidationExceptionMapperImpl;
import gov.ca.cwds.rest.filters.WebSecurityFilter;
import gov.ca.cwds.rest.resources.SwaggerResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

/**
 * Base execution class for CWDS REST API applications.
 * 
 * @author CWDS API Team
 * @param <T> type of configuration
 */
public abstract class BaseApiApplication<T extends MinimalApiConfiguration> extends Application<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseApiApplication.class);

  protected GuiceBundle<T> guiceBundle;

  private static Injector injector;

  private final ShiroBundle<T> shiroBundle = new ShiroBundle<T>() {
    @Override
    protected ShiroConfiguration narrow(T configuration) {
      return configuration.getShiroConfiguration();
    }
  };

  /**
   * Extending classes must provide the Guice module for their application.
   * 
   * @param bootstrap The bootstrap
   * @return The main Guice module for this application.
   */
  public abstract Module applicationModule(Bootstrap<T> bootstrap);

  /**
   * Extending classes can do any initialization specific to their applications here.
   * 
   * @param bootstrap The bootstrap for this application
   */
  public void initializeInternal(Bootstrap<T> bootstrap) {}

  /**
   * Extending cannot implement the run method, this is their chance to complete tasks which would
   * normally go in run.
   * 
   * @param configuration The configuration
   * @param environment The environment
   */
  public void runInternal(final T configuration, final Environment environment) {}

  @Override
  public final void initialize(Bootstrap<T> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

    bootstrap.addBundle(new ViewBundle<T>());
    guiceBundle = GuiceBundle.<T>newBuilder().addModule(applicationModule(bootstrap))
        .setConfigClass(bootstrap.getApplication().getConfigurationClass())
        .enableAutoConfig(getClass().getPackage().getName()).build();

    bootstrap.addBundle(guiceBundle);
    bootstrap.addBundle(shiroBundle);
    initializeInternal(bootstrap);
  }

  @Override
  public final void run(final T configuration, final Environment environment) throws Exception {
    environment.servlets().setSessionHandler(new SessionHandler());

    registerExceptionMappers(environment);

    LOGGER.info("Application name: {}, Version: {}", configuration.getApplicationName(),
        configuration.getVersion());

    LOGGER.info("Configuring CORS: Cross-Origin Resource Sharing");
    configureCors(environment);

    LOGGER.info("Configuring ObjectMapper");
    ObjectMapperUtils.configureObjectMapper(environment.getObjectMapper());

    LOGGER.info("Configuring SWAGGER");
    configureSwagger(configuration, environment);

    LOGGER.info("Registering Filters");
    registerFilters(environment, guiceBundle);

    LOGGER.info("Registering SystemCodeCache");
    runInternal(configuration, environment);
  }

  private void registerExceptionMappers(Environment environment) {
    LoggingContext loggingContext = guiceBundle.getInjector().getInstance(LoggingContext.class);
    environment.jersey().register(new ApiSecurityExceptionMapper());
    environment.jersey().register(new UnexpectedExceptionMapperImpl(loggingContext));
    environment.jersey().register(new ExpectedExceptionMapperImpl(loggingContext));
    environment.jersey().register(new ValidationExceptionMapperImpl(loggingContext));
    environment.jersey().register(new BusinessValidationExceptionMapper());
    environment.jersey().register(new ReferentialIntegrityExceptionMapper(loggingContext));
    environment.jersey().register(new DaoExceptionMapper(loggingContext));
    environment.jersey().register(new ServiceExceptionMapper(loggingContext));
    environment.jersey().register(new CustomExceptionMapperBinder(loggingContext, true));
  }

  /**
   * Register filters in {@link FilterModule.configure} and configure them here.
   * 
   * @param environment
   */
  private static void registerFilters(final Environment environment, GuiceBundle guiceBundle) {
    BaseApiApplication.injector = guiceBundle.getInjector();

    environment.servlets()
        .addFilter("WebSecurityFilter", injector.getInstance(WebSecurityFilter.class))
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
  }

  private static void configureCors(final Environment environment) {
    FilterRegistration.Dynamic filter =
        environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter("allowedHeaders",
        "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,X-Auth-Token");
    filter.setInitParameter("allowCredentials", "true");
  }

  private void configureSwagger(final T apiConfiguration, final Environment environment) {
    BeanConfig config = new BeanConfig();
    SwaggerConfiguration swaggerConfiguration = apiConfiguration.getSwaggerConfiguration();
    config.setTitle(swaggerConfiguration.getTitle());
    config.setDescription(swaggerConfiguration.getDescription());
    config.setResourcePackage(swaggerConfiguration.getResourcePackage());
    config.setScan(true);

    new AssetsBundle(swaggerConfiguration.getAssetsPath(),
        swaggerConfiguration.getAssetsPath(), null, "swagger")
            .run(environment);

    LOGGER.info("Registering ApiListingResource");
    environment.jersey().register(new ApiListingResource());

    LOGGER.info("Registering SwaggerResource");
    SwaggerResource swaggerResource = new SwaggerResource(swaggerConfiguration);
    environment.jersey().register(swaggerResource);

    TokenResource tokenResource = new TokenResource(swaggerConfiguration);
    environment.jersey().register(tokenResource);
  }

  @SuppressWarnings("javadoc")
  public static Injector getInjector() {
    return injector;
  }

}
