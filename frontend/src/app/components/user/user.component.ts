import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  error: boolean = false;
  errorMessage: string = '';
  users: User[];
  userForm: FormGroup;
  submitted: boolean = false;
  headElements = ['Id', 'Name', 'Type', 'User Since', 'Last Login'];

  constructor(private authService: AuthService, private userService: UserService, private formBuilder: FormBuilder) {
    this.userForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      type: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.loadUsers();
  }

  getUsers(): User[] {
    return this.users;
  }

  /**
   * Load all users from the backend
   */
  loadUsers() {
    this.userService.getAllUsers().subscribe(
      (users: User[]) => { this.users = users; },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }

  /**
   * Starts form validation and builds a user dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addUser() {
    this.submitted = true;
    if (this.userForm.valid && (this.userForm.controls.type.value === 'ADMIN' || this.userForm.controls.type.value === 'SELLER')) {
      const user: User = new User(null,
        this.userForm.controls.name.value,
        this.userForm.controls.type.value,
        new Date().toISOString(),
        null
      );
      this.createUser(user);
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  createUser(user: User) {
    this.userService.createUser(user).subscribe(
      () => { this.loadUsers(); },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.error;
    }
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.userForm.reset();
    this.submitted = false;
  }

}
