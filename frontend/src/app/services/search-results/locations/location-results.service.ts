import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Location} from '../../../dtos/location';

@Injectable({
  providedIn: 'root'
})
export class LocationResultsService {

  private locationsBaseUri: string = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  public findLocationsFiltered(country, city, street, postalCode, description): Observable<Location[]> {
    console.log('Location Service: findLocationsFiltered');
    return this.httpClient.get<Location[]>(this.locationsBaseUri, {params: { country: country, city: city, street: street, postalCode: postalCode, description: description }});
  }

  public getCountriesOrderedByName(): Observable<string[]> {
    console.log('Location Service: getCountriesOrderedByName');
    return this.httpClient.get<string[]>(this.locationsBaseUri + '/countries');
  }
}
