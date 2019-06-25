import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserType} from '../../datatype/user_type';
import {ChangePasswordRequest} from '../../dtos/change-password-request';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;
  private error: boolean = false;
  private userBlocked: boolean = false;
  private userAlreadyBlocked: boolean = false;
  private cantBlockAdmin: boolean = false;
  private errorMessage: string = '';
  private blockedUserMessage: string = 'User was successfully blocked!';
  private passwordResetMessage: string = 'Password was successfully reset';
  private users: User[];
  private userForm: FormGroup;
  private submitted: boolean = false;
  private usernameError: boolean = false;
  private headElements = ['Username', 'Type', 'User Since', 'Last Login', 'Remove', 'Block', 'Reset Password'];
  private userTypes = ['Admin', 'Seller'];
  private selectedUserType: string = null;
  private userToDelete: number = null;
  private userToSearch: string = null;
  private userToChangePwd: User;
  private passwordChangeAttempt: boolean = false;
  private passwordChangeRequest: ChangePasswordRequest;
  private passwordReset: boolean = false;


  constructor(private authService: AuthService, private userService: UserService, private formBuilder: FormBuilder) {
    this.userForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.compose([Validators.required, Validators.minLength(8)])]],
      rePassword: ['', []],
      type: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.loadUsers(null);
  }

  /**
   * Load all users from the backend
   */
  private loadUsers(username: string) {
    console.log('Get users');
    this.userService.getUsers(username, this.page).subscribe(
      result => {
        this.users = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {},
      () => {
        this.dataReady = true;
      }
    );
  }

  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadUsers(null);
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event to handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadUsers(null);
    }
  }

  /**
   * Sets page number to the next one and calls the last method
   * @param event to handle
   */
  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadUsers(null);
    }
  }

  /**
   * Determines the page numbers which will be shown in the clickable menu
   */
  private setPagesRange() {
    this.pageRange = []; // nullifies the array
    if (this.totalPages <= 11) {
      for (let i = 0; i < this.totalPages; i++) {
        this.pageRange.push(i);
      }
    } else {
      if (this.page <= 5) {
        for (let i = 0; i <= 10; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page > 5 && this.page < this.totalPages - 5) {
        for (let i = this.page - 5; i <= this.page + 5; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page >= this.totalPages - 5) {
        for (let i = this.totalPages - 10; i < this.totalPages; i++) {
          this.pageRange.push(i);
        }
      }
    }
  }

  /**
   * Starts form validation and builds a user dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  private async addUser() {
    this.submitted = true;
    this.usernameError = false;
    if (this.userForm.valid
      && this.userForm.controls.rePassword.value === this.userForm.controls.password.value
      && this.selectedUserType != null) {
      const user: User = new User(null,
        this.userForm.controls.username.value,
        this.userForm.controls.password.value,
        UserType[this.selectedUserType.toUpperCase()],
        new Date().toISOString(),
        null,
        null
      );
      this.createUser(user);
      await this.delay(300);
      if (this.usernameError) {
        console.log('User with the same username already exists');
        return;
      }
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  private createUser(user: User) {
    this.userService.createUser(user).subscribe(
      (newUser: User) => { if (newUser.id === -1) { this.usernameError = true; } },
      error => { this.loadUsers(null); },
      () => { this.loadUsers(null); }
    );
  }

  /**
   * blocks a user given by id
   * @param userId the id of the user that is to be blocked
   */
  private blockUser(userId: number) {
    this.userService.blockUser(userId).subscribe(
      () => {},
      error => {},
      () => { this.loadUsers(this.userToSearch); this.showUserBlockedMessage(); }
    );
  }

  private userIsAdmin(user: User): boolean {
    return user.type === UserType.ADMIN;
  }

  private deleteUser(userId: number) {
    this.userToDelete = null;
    this.userService.deleteUser(userId).subscribe(
      () => {},
      error => {},
      () => { this.loadUsers(null); }
    );
  }

  private setUserToDelete(userId: number) {
    this.userToDelete = userId;
  }
  private setUserToReset(user: User) {
    this.userToChangePwd = user;
  }

  /**
   * sends an request to the backend to change the password of a user
   * @param changePasswordRequest an request containing id name and the new password
   */
  private changePassword(newPassword: string): boolean {
    this.passwordChangeAttempt = false;
    this.passwordChangeRequest = new ChangePasswordRequest(this.userToChangePwd.id, this.userToChangePwd.username, newPassword);
    this.userService.changePassword(this.passwordChangeRequest).subscribe();
    this.showPasswordResetMessage();
    return true;
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  private isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  private vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.userForm.reset();
    this.submitted = false;
  }

  private delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  private handleBlockError(error: any) {
    if (error.error.errors[0].includes('Admin')) {
      this.showCantBlockAdminMessage();
    } else if (error.error.errors[0].includes('already')) {
      this.showUserAlreadyBlockedMessage();
    }
  }

  private showUserBlockedMessage() {
    this.userBlocked = true;
    setTimeout(() => this.userBlocked = false, 5000);
  }

  private showCantBlockAdminMessage() {
    this.cantBlockAdmin = true;
    setTimeout(() => this.cantBlockAdmin = false, 5000);
  }
  private showPasswordResetMessage() {
    this.passwordReset = true;
    setTimeout(() => this.passwordReset = false, 5000);
  }

  private showUserAlreadyBlockedMessage() {
    this.userAlreadyBlocked = true;
    setTimeout(() => this.userAlreadyBlocked = false, 5000);
  }
  private checkIfUserIsAdmin(user: User): boolean {
    return user.type.toString() === 'ADMIN';
  }
  private setPasswordChangeAttempt() {
    this.passwordChangeAttempt = true;
  }

}
