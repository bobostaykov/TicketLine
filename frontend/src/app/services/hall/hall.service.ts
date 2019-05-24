import { Injectable } from '@angular/core';
import { Globals } from '../../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Hall} from '../../dtos/hall';

@Injectable({
  providedIn: 'root'
})
export class HallService {

  private hallBaseUri: string = this.globals.backendUri + '/halls';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all halls from backend
   */
  getAllHalls(): Observable<Hall[]> {
    return this.httpClient.get<Hall[]>(this.hallBaseUri);
  }
}
