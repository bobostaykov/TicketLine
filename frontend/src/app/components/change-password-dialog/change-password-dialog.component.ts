import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output, SimpleChanges
} from '@angular/core';
import {ChangePasswordRequest} from '../../dtos/change-password-request';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../services/user/user.service';
import {AuthService} from '../../services/auth/auth.service';
import {User} from '../../dtos/user';


@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})

export class ChangePasswordDialogComponent implements OnInit {
  private changePasswordRequest: ChangePasswordRequest = new ChangePasswordRequest(null, null, null);
  private newPassword: string;
  private submitted: boolean;
  @Input() user: User;
  @Output() submitPasswordChangeRequest = new EventEmitter<ChangePasswordRequest>();
  private passwordForm: FormGroup;


  private onSubmit() {
    this.changePasswordRequest.id = this.user.id;
    this.changePasswordRequest.username = this.user.username;
    this.changePasswordRequest.password = this.passwordForm.controls.newPassword.value;
    this.submitPasswordChangeRequest.emit(this.changePasswordRequest);
  }
  constructor(private authService: AuthService, private userService: UserService, private formBuilder: FormBuilder) {
    this.passwordForm = formBuilder.group(
      {newPassword: ['', [Validators.compose([Validators.required, Validators.minLength(8)])]],
      rePassword: ['', []]});
  }
  ngOnInit(): void {
    console.log('username is : ' + this.user.username);
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
