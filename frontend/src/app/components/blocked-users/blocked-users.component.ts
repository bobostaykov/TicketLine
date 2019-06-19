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

  private page: number = 0;
  private pages: Array<number>;
  private dataReady: boolean = false;

  private blockedUsers: User[];
  private headElements = ['Username', 'Type', 'User Since', 'Last Login', 'Delete' , 'Unblock'];
  private error: boolean = false;
  private errorMessage: string = '';
  private userToDelete: number = null;

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


  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadBlockedUsers();
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event o handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadBlockedUsers();
    }
  }

  /**
   * Sets page number to the next one and calls the last method
   * @param event to handle
   */
  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.pages.length - 1) {
      this.page++;
      this.loadBlockedUsers();
    }
  }

  private loadBlockedUsers() {
    console.log('get all blocked users');
    this.userService.getAllBlockedUsers(this.page).subscribe(
      result => {
        this.blockedUsers = result['content'];
        this.pages = new Array(result['totalPages']);
      },
      error => this.defaultServiceErrorHandling(error),
      () => {
        this.dataReady = true;
        if (this.blockedUsers.length === 0 && this.pages.length === 1) {
          this.page--;
          this.loadBlockedUsers();
        }
      }
    );
  }

  private unblockUser(user: User) {
    this.userService.unblockUser(user.id).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => {
          this.loadBlockedUsers();
      }
    );
  }

  private deleteUser(userId: number) {
    this.userToDelete = null;
    this.userService.deleteUser(userId).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadBlockedUsers(); }
    );
  }

  private setUserToDelete(userId: number) {
    this.userToDelete = userId;
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
