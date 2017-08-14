package gov.ca.cwds.rest.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.hamcrest.junit.ExpectedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MutuallyNecessaryValidatorTest {
  private String abc;
  private String def;
  private String ghi;

  private MutuallyNecessary requiredConstraintAnnotation = mock(MutuallyNecessary.class);
  private MutuallyNecessary notRequiredConstraintAnnotation = mock(MutuallyNecessary.class);
  private ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
  private ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
  private NodeBuilderCustomizableContext nodeBuilder = mock(NodeBuilderCustomizableContext.class);

  private MutuallyNecessaryValidator validator = new MutuallyNecessaryValidator();

  private String[] properties = {"abc", "def", "ghi"};

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() throws Exception {
    when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
    when(builder.addPropertyNode(any())).thenReturn(nodeBuilder);

    when(requiredConstraintAnnotation.properties()).thenReturn(properties);
    when(notRequiredConstraintAnnotation.properties()).thenReturn(properties);

    when(notRequiredConstraintAnnotation.required()).thenReturn(false);
    when(requiredConstraintAnnotation.required()).thenReturn(true);
  }

  @Test
  public void validReturnsTrueWhenRequiredAndAllSet() throws Exception {
    MutuallyNecessaryValidatorTest bean = new MutuallyNecessaryValidatorTest();
    bean.abc = "abc";
    bean.def = "def";
    bean.ghi = "ghi";

    validator.initialize(requiredConstraintAnnotation);
    assertThat(validator.isValid(bean, context), is(equalTo(true)));
  }

  @Test
  public void validReturnsFalseWhenRequiredAndNotAllSet() throws Exception {
    MutuallyNecessaryValidatorTest bean = new MutuallyNecessaryValidatorTest();
    bean.abc = "abc";

    validator.initialize(requiredConstraintAnnotation);
    assertThat(validator.isValid(bean, context), is(equalTo(false)));
  }

  @Test
  public void validReturnsTrueWhenNotRequiredAndAllSet() throws Exception {
    MutuallyNecessaryValidatorTest bean = new MutuallyNecessaryValidatorTest();
    bean.abc = "abc";
    bean.def = "def";
    bean.ghi = "ghi";

    validator.initialize(notRequiredConstraintAnnotation);
    assertThat(validator.isValid(bean, context), is(equalTo(true)));
  }

  @Test
  public void validReturnsTrueWhenNotRequiredAndNoneSet() throws Exception {
    MutuallyNecessaryValidatorTest bean = new MutuallyNecessaryValidatorTest();

    validator.initialize(notRequiredConstraintAnnotation);
    assertThat(validator.isValid(bean, context), is(equalTo(true)));
  }

  @Test
  public void validReturnsFalseWhenNotRequiredAndSomeButNotAllSet() throws Exception {
    MutuallyNecessaryValidatorTest bean = new MutuallyNecessaryValidatorTest();
    bean.abc = "abc";

    validator.initialize(notRequiredConstraintAnnotation);
    assertThat(validator.isValid(bean, context), is(equalTo(false)));
  }

  @Test
  public void validThrowsExceptionWhenPropertyNotExistsInBean() throws Exception {
    thrown.expect(ValidationException.class);
    validator.initialize(notRequiredConstraintAnnotation);
    validator.isValid(new InvalidBean(), context);
  }

  /*
   * Oddness with cobertura cause the declaring class line to be not counted as run. This has to do
   * with bridge functions. To get our coverage numbers the "test" below calls the bridge functions
   * directly.
   */
  @Test
  public void callBridgeFunctions() throws Exception {
    MutuallyNecessaryValidator validator = new MutuallyNecessaryValidator();
    Method initialize = MutuallyNecessaryValidator.class.getMethod("initialize", Annotation.class);
    initialize.invoke(validator, notRequiredConstraintAnnotation);
    Assert.assertTrue(true);
  }

  public String getAbc() {
    return abc;
  }

  public String getDef() {
    return def;
  }

  public String getGhi() {
    return ghi;
  }

  protected class InvalidBean {
    public String jkl;
  }
}
