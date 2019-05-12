import { Component, OnInit } from '@angular/core';
import { AuthService} from '../../services/auth/auth.service';
import {UserService} from '../../services/user/user.service';
import {User} from '../../dtos/user';

// todo Documentation und logging

@Component({
  selector: 'app-blocked-users',
  templateUrl: './blocked-users.component.html',
  styleUrls: ['./blocked-users.component.scss']
})
export class BlockedUsersComponent implements OnInit {

  blockedUsers: User[];
  headElements = ['Id', 'Name', 'Type'];

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

  private getBlockedUsers(){
    return this.loadBlockedUsers();
  }

  private loadBlockedUsers() {
    this.userService.getAllBlockedUsers().subscribe(
      (users: User[]) => {this.blockedUsers = users; }, error => console.log(error));
  }
  private unblockUser(user: User) {
    this.userService.unblockUser(user);
}
}

