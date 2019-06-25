import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {User} from '../../dtos/user';
import {ChangePasswordRequest} from '../../dtos/change-password-request';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/users';
  private blockedUserBaseUri: string = this.userBaseUri + '/blocked';
  private unblockUserBaseUri: string = this.blockedUserBaseUri + '/unblock';
  private passWordChangeBaseUri: string = this.userBaseUri + '/password';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Loads all users from the backend, or users that have 'username' in their username, if username !== null
   * @param username string to search by
   * @param page the number of the requested page
   */
  getUsers(username: string, page: number): Observable<User[]> {
    console.log('Get users');
    return this.httpClient.get<User[]>(this.userBaseUri, {params: { username: username, page: page.toString() }});
  }

  /**
   * Loads all blocked users from the backend, or blocked users that have 'username' in their username, if username !== null
   * @param username string to search by
   * @param page the number of the requested page
   */
  getBlockedUsers(username: string, page: number): Observable<User[]> {
    console.log('Get blocked users');
    return this.httpClient.get<User[]>(this.blockedUserBaseUri, {params: { username: username, page: page.toString() }});
  }

  /**
   * unblocks the specified user
   * @param userId id of an user
   */
  unblockUser(userId: number): Observable<{}> {
    console.log('unblocking user with id ' + userId);
    return this.httpClient.put(this.unblockUserBaseUri + '/' + userId, null);
  }


  blockUser(userId: number): Observable<{}> {
    console.log('blocking user with id ' + userId);
    return this.httpClient.put(this.blockedUserBaseUri + '/' + userId, null);
  }


  /**
   * Loads specific user from the backend
   * @param userId of user to load
   */
  getUserById(userId: number): Observable<User> {
    console.log('Load user details for ' + userId);
    return this.httpClient.get<User>(this.userBaseUri + '/' + userId);
  }

  /**
   * Persists user to the backend
   * @param user to persist
   */
  createUser(user: User): Observable<User> {
    console.log('Create user with username ' + user.username);
    return this.httpClient.post<User>(this.userBaseUri, user);
  }

  /**
   * Deletes the user with the specified id
   * @param userId id of user to delete
   */
  deleteUser(userId: number): Observable<{}> {
    console.log('Delete user with id ' + userId);
    return this.httpClient.delete(this.userBaseUri + '/' + userId);
  }

  /**
   * Changes the password of a user (sender has to be admin and only passwords of not-admin users can be changed
   * @param changePasswordRequest a request containing the id, the username and the new password
   */
  changePassword(changePasswordRequest: ChangePasswordRequest): Observable<{}> {
    console.log('Change Password for user' + changePasswordRequest.username);
    console.log('POST' + this.passWordChangeBaseUri)
    return this.httpClient.post<ChangePasswordRequest>(this.passWordChangeBaseUri, changePasswordRequest);
  }

}
