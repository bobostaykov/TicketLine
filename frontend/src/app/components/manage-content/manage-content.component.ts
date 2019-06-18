import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-manage-content',
  templateUrl: './manage-content.component.html',
  styleUrls: ['./manage-content.component.scss']
})
export class ManageContentComponent implements OnInit {

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  private isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

}
