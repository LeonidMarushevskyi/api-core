package gov.ca.cwds.rest.validation;

import java.text.MessageFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates that the {@code OnlyIf.property} of a given bean is not set unless the
 * {@code Onlyif.ifProperty} is also not empty.
 * 
 * @author CWDS API Team
 */
public class OnlyIfValidator implements AbstractBeanValidator, ConstraintValidator<OnlyIf, Object> {

  @SuppressWarnings("unused")
  private static final Logger LOGGER = LoggerFactory.getLogger(OnlyIfValidator.class);

  private String ifProperty;
  private String property;

  @Override
  public void initialize(OnlyIf anno) {
    this.ifProperty = anno.ifProperty();
    this.property = anno.property();
  }

  @Override
  public boolean isValid(final Object bean, ConstraintValidatorContext context) {
    boolean valid = true;

    String ifValue = readBeanValue(bean, ifProperty);
    boolean ifValueBlank = StringUtils.isBlank(ifValue);

    if (ifValueBlank) {
      String value = readBeanValue(bean, property);
      if (StringUtils.isNotBlank(value)) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(
                MessageFormat.format("can only be set if {0} is set", ifProperty))
            .addPropertyNode(property).addConstraintViolation();
        valid = false;
      }
    }

    return valid;
  }
}
