import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Location} from '../../../dtos/location';

@Injectable({
  providedIn: 'root'
})
export class LocationsService {

  private locationsBaseUri: string = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  public findLocationsFiltered(country, city, street, postalCode, description): Observable<Location[]> {
    console.log('Service: findLocationsFiltered');
    return this.httpClient.get<Location[]>(this.locationsBaseUri, {params: { country: country, city: city, street: street, postalCode: postalCode, description: description }});
  }
}
