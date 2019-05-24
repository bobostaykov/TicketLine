import { Injectable } from '@angular/core';
import {Globals} from '../../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Location} from '../../dtos/location';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  private locationBaseUri = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Loads all locations from backend
   */
  getAllLocations(): Observable<Location[]> {
    return this.httpClient.get<Location[]>(this.locationBaseUri);
  }
}
