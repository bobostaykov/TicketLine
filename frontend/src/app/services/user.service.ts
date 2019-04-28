import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {User} from '../dtos/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Loads all users from the backend
   */
  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userBaseUri);
  }

  /**
   * Loads specific user from the backend
   * @param id of user to load
   */
  getUserById(id: number): Observable<User> {
    console.log('Load user details for ' + id);
    return this.httpClient.get<User>(this.userBaseUri + '/' + id);
  }

  /**
   * Persists user to the backend
   * @param user to persist
   */
  createUser(user: User): Observable<User> {
    console.log('Create user with name ' + user.name);
    return this.httpClient.post<User>(this.userBaseUri, user);
  }

}
