package at.spengergasse.spengerfhirmed.Validator;

// ✅ KORRIGIERT: jakarta statt javax
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SVNRValidator.class})
@Documented
public @interface SVNRValid {
    String message() default "Patient.deceasedBoolean and Patient.deceasedDateTime must not both have a value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}