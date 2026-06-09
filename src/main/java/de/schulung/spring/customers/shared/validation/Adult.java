package de.schulung.spring.customers.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.FIELD,
  ElementType.PARAMETER,
  ElementType.ANNOTATION_TYPE,
  ElementType.METHOD,
  ElementType.CONSTRUCTOR,
  ElementType.TYPE_USE,
})
@Documented
// Bean Validation
@Constraint(validatedBy = AdultValidator.class)
public @interface Adult {

  String message() default "must be an adult";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Specifies the minimum required age for validation. The default value is 18.
   *
   * @return the minimum age required for validation
   */
  long value() default 18;

  /**
   * Specifies the unit of time used to calculate the age for validation purposes.
   * The default unit is years, represented by ChronoUnit.YEARS.
   *
   * @return the ChronoUnit used to calculate the age
   */
  ChronoUnit unit() default ChronoUnit.YEARS;
}
