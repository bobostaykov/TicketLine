import { Component, OnInit } from '@angular/core';
import { AuthService} from '../../services/auth/auth.service';
import {UserService} from '../../services/user/user.service';
import {User} from '../../dtos/user';
import {ChangePasswordRequest} from '../../dtos/change-password-request';
import {UserType} from '../../datatype/user_type';


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
  private userUnblocked: boolean = false;
  private errorMessage: string = '';
  private unblockedUserMessage: string = 'User was successfully unblocked!';
  private passwordResetMessage: string = 'Password was successfully reset';
  private userToDelete: number = null;
  private userToSearch: string = null;
  private userToChangePwd: User;
  private passwordChangeAttempt: boolean = false;
  private passwordChangeRequest: ChangePasswordRequest;
  private passwordReset: boolean = false;
  constructor(private authService: AuthService, private userService: UserService) {
  }

  ngOnInit() {
    if (this.isAdmin()) {
      this.loadBlockedUsers(null);
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
    this.loadBlockedUsers(null);
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event o handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadBlockedUsers(null);
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
      this.loadBlockedUsers(null);
    }
  }

  /**
   * sends an request to the backend to change the password of a user
   * @param changePasswordRequest an request containing id name and the new password
   */
  private changePassword(newPassword: string): boolean{
    this.passwordChangeAttempt = false;
    this.passwordChangeRequest = new ChangePasswordRequest(this.userToChangePwd.id, this.userToChangePwd.username, newPassword)
    this.userService.changePassword(this.passwordChangeRequest).subscribe();
    this.showPasswordResetMessage();
    return true;
  }

  /**
   * loads a page of blocked users matching the name
   * @param username the name
   */
  private loadBlockedUsers(username: string) {
    console.log('Get blocked users');
    this.userService.getBlockedUsers(username, this.page).subscribe(
      result => {
        this.blockedUsers = result['content'];
        this.pages = new Array(result['totalPages']);
      },
      error => this.defaultServiceErrorHandling(error),
      () => {
        this.dataReady = true;
        if (this.blockedUsers.length === 0 && this.pages.length === 1) {
          this.page--;
          this.loadBlockedUsers(null);
        }
      }
    );
  }

  /**
   * unblocks a user
   * @param user the user that is to be unblocked
   */
  private unblockUser(user: User) {
    this.userService.unblockUser(user.id).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadBlockedUsers(this.userToSearch); this.showUserUnblockedMessage(); }
    );
  }

  /**
   * deletes a user
   * @param userId the id of a user
   */
  private deleteUser(userId: number) {
    this.userToDelete = null;
    this.userService.deleteUser(userId).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadBlockedUsers(null); }
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

  private showUserUnblockedMessage() {
    this.userUnblocked = true;
    setTimeout(() => this.userUnblocked = false, 5000);
  }
  private showPasswordResetMessage() {
    this.passwordReset = true;
    setTimeout(() => this.passwordReset = false, 5000);
  }
  private checkIfUserIsAdmin(user: User): boolean{
    return user.type === UserType.ADMIN;
  }
  private setPasswordChangeAttempt() {
    this.passwordChangeAttempt = true;
  }
  private setUserToReset(user: User) {
    this.userToChangePwd = user;
  }

}
