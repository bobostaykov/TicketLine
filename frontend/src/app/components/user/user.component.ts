import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserType} from '../../datatype/user_type';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';
  private users: User[];
  private userForm: FormGroup;
  private submitted: boolean = false;
  private usernameError: boolean = false;
  private headElements = ['Id', 'Name', 'Type', 'User Since', 'Last Login', 'Remove'];
  private userTypes = ['Admin', 'Seller'];
  private selectedUserType: string = null;
  private userToDelete: number = null;

  constructor(private authService: AuthService, private userService: UserService, private formBuilder: FormBuilder) {
    this.userForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.compose([Validators.required, Validators.minLength(8)])]],
      rePassword: ['', []],
      type: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.loadUsers();
  }

  /**
   * Load all users from the backend
   */
  private loadUsers() {
    this.userService.getAllUsers().subscribe(
      (users: User[]) => { this.users = users; },
      error => { this.defaultServiceErrorHandling(error); }
    );
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
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadUsers(); }
    );
  }

  /**
   * blocks a user given by id
   * @param userId the id of the user that is to be blocked
   */
  private blockUser(userId: number) {
    this.userService.blockUser(userId).subscribe(
      () => {},
      err => { this.errorMessage = 'cant block admin'},
      () => { this.loadUsers(); }
    );
  }

  private deleteUser(userId: number) {
    this.userToDelete = null;
    this.userService.deleteUser(userId).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadUsers(); }
    );
  }

  private setUserToDelete(userId: number) {
    this.userToDelete = userId;
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  private isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.news !== 'No message available') {
      this.errorMessage = error.error.news;
    } else if (error.error.httpRequestStatusCode === 404) {
      this.errorMessage = 'could not block user';
    } else {
      this.errorMessage = error.error.error;
    }
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

}
