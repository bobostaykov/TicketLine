package at.ac.tuwien.sepm.groupphase.backend.annotation;

import at.ac.tuwien.sepm.groupphase.backend.annotation.implementation.SeatsXorSectorsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation to validate hallDTO
 * checks if one of seats or sectors but not both are set within hallDTO
 */
@Documented
@Constraint(validatedBy = SeatsXorSectorsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SeatsXorSectorsConstraint {
    String message() default "Either seats or sectors but not both must be set";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
