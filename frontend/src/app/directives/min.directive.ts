import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn, Validators} from '@angular/forms';
import {Directive, forwardRef, Input} from '@angular/core';

@Directive({
  selector: '[appMin][ngModel]',
  providers: [{provide: NG_VALIDATORS, useExisting: forwardRef(() => MinDirective), multi: true}]
})
export class MinDirective implements Validator {
  private _validator: ValidatorFn;
  @Input() public set min(value: string) {
    this._validator = Validators.min(parseInt(value, 10));
  }
  public validate(control: AbstractControl): ValidationErrors | null {
    return this._validator(control);
  }
}
