import {AbstractControl} from '@angular/forms';
import {Hall} from '../dtos/hall';

export function ValidateSeatOrSector(control: AbstractControl) {
  const hall: Hall = control.value;
  if (hall.seats && hall.seats.length > 0 || hall.sectors && hall.sectors.length > 0) {
    return null;
  } else {
    return {'missingSeats': true};
  }
}
