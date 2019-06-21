package at.ac.tuwien.sepm.groupphase.backend.annotation.implementation;

import at.ac.tuwien.sepm.groupphase.backend.annotation.SeatsXorSectorsConstraint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.CollectionUtils.isEmpty;

public class SeatsXorSectorsValidator implements ConstraintValidator<SeatsXorSectorsConstraint, HallDTO> {

    @Override
    public void initialize(SeatsXorSectorsConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(HallDTO hallDTO, ConstraintValidatorContext constraintValidatorContext) {
        return isEmpty(hallDTO.getSeats()) && ! isEmpty(hallDTO.getSectors()) ||
            ! isEmpty(hallDTO.getSeats()) && isEmpty(hallDTO.getSectors());
    }
}
