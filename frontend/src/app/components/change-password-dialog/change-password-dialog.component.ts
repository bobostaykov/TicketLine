import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output
} from '@angular/core';
import {ChangePasswordRequest} from '../../dtos/change-password-request';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../services/user/user.service';
import {AuthService} from '../../services/auth/auth.service';


@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})

export class ChangePasswordDialogComponent implements OnInit {
  private changePasswordRequest: ChangePasswordRequest = new ChangePasswordRequest(null, null, null);
  private newPassword: string = '';
  private submitted: boolean;
  @Output() submitNewPassword = new EventEmitter<String>();
  private passwordForm: FormGroup;


  private onSubmit() {
    this.submitted = true
    if (this.passwordForm.valid) {
      this.newPassword = this.passwordForm.controls.newPassword.value;
      this.submitNewPassword.emit(this.newPassword);
      this.submitted = false;
    }
  }
  constructor(private authService: AuthService, private userService: UserService, private formBuilder: FormBuilder) {
    this.passwordForm = formBuilder.group(
      {newPassword: ['', [Validators.compose([Validators.required, Validators.minLength(8)])]],
      rePassword: ['', []]});
  }
  ngOnInit(): void {
  }

/*
  ngOnChanges(changes: SimpleChanges): void {
    if()
    for(let propName in changes){
      if(propName === 'id') {
        this.id = curVal.;
      }
      let change = changes[propName];
      let curVal = JSON.stringify(change.currentValue);

      if(propName === 'userName'){
        this.name = curVal;
      }
    }

    if(changes['id']){
      if(this.id !== undefined){
        for(let id in changes){
          let change = changes['id'];

        }
      }
    }
  }*/
  changePassword() {
    this.submitted = true;
    if (this.passwordForm.valid
      && this.passwordForm.controls.rePassword.value === this.passwordForm.controls.newPassword.value) {
      this.onSubmit();
    }
  }
}
