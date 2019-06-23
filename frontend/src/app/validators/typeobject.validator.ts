import {AbstractControl} from '@angular/forms';

export function ValidateTypeofObject(control: AbstractControl) {
  return typeof control.value === 'object' ? null : {validType: true};
}
