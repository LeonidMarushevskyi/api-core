package gov.ca.cwds.rest.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation at the class level indicating that one of the given properties is required however
 * mutually exclusive.
 * 
 * @author CWDS API Team
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MutuallyExclusiveValidator.class)
public @interface MutuallyExclusive {
  String message() default "The following properties are mutually exclusive {properties}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] properties() default {};

  boolean required() default true;

  @SuppressWarnings("rawtypes")
  Class type() default String.class;
}
