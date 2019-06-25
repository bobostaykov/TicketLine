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

  private changePassword() {
    console.log('changing password')
    this.submitted = true;
    this.onSubmit();
  }
  private setSubmitted(){
    this.submitted = true;
  }
}
