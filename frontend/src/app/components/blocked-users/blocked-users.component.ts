import { Component, OnInit } from '@angular/core';
import { AuthService} from '../../services/auth/auth.service';
import {UserService} from '../../services/user/user.service';
import {User} from '../../dtos/user';


@Component({
  selector: 'app-blocked-users',
  templateUrl: './blocked-users.component.html',
  styleUrls: ['./blocked-users.component.scss']
})
export class BlockedUsersComponent implements OnInit {

  blockedUsers: User[];
  headElements = ['Id', 'Name', 'Type', 'User Since', 'Last Login', 'Delete' ,'Unblock' ];
  error: boolean = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private userService: UserService) {
  }

  ngOnInit() {
    if (this.isAdmin()) {
      this.loadBlockedUsers();
    }
  }

  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }
  private vanishError() {
    this.error = false;
  }
  private loadBlockedUsers() {
    console.log('get all blocked users')
    this.userService.getAllBlockedUsers().subscribe(
      (users: User[]) => { this.blockedUsers = users; },
      error => console.log(error));
  }
  private unblockUser(user: User) {
    this.userService.unblockUser(user.id).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadBlockedUsers(); }
    );
  }
  private deleteUser(userId: number) {
    this.userService.deleteUser(userId).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadBlockedUsers(); }
    );
  }
  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.news !== 'No message available') {
      this.errorMessage = error.error.news;
    } else {
      this.errorMessage = error.error.error;
    }
  }
}
