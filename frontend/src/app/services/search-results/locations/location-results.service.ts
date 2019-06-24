import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Location} from '../../../dtos/location';

@Injectable({
  providedIn: 'root'
})
export class LocationResultsService {

  private locationsBaseUri: string = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Updates specified location in backend
   * @param location location dto with changed attributes
   */
  public updateLocation(location: Location): Observable<{}> {
    console.log('LocationResultsService: updateLocation');
    return this.httpClient.put<Location>(this.locationsBaseUri + '/' + location.id, location);
  }

  /**
   * Adds a location to the data base
   * @param location to persist
   */
  public addLocation(location: Location): Observable<{}> {
    console.log('LocationResultsService: addLocation');
    return this.httpClient.post<Location>(this.locationsBaseUri, location);
  }

  /**
   * Deletes specified location in backend
   * @param locationId of the location to delete
   */
  public deleteLocation(locationId: number): Observable<{}> {
    console.log('LocationResultsService: deleteLocation');
    return this.httpClient.delete(this.locationsBaseUri + '/' + locationId);
  }

  /**
   * Searches for location filtered by:
   * @param name of the location
   * @param country of the location
   * @param city of the location
   * @param street of the location
   * @param postalCode of the location
   * @param description of the location
   * @param page the number of the page to return
   */
  public findLocationsFiltered(name, country, city, street, postalCode, description, page): Observable<Location[]> {
    console.log('Location Service: findLocationsFiltered');
    let parameters = new HttpParams();
    parameters = name ? parameters.append('name', name) : parameters;
    parameters = country ? parameters.append('country', country) : parameters;
    parameters = city ? parameters.append('city', city) : parameters;
    parameters = street ? parameters.append('street', street) : parameters;
    parameters = postalCode ? parameters.append('postalCode', postalCode) : parameters;
    parameters = description ? parameters.append('postalCode', description) : parameters;
    parameters = parameters.append('page', page);
    return this.httpClient.get<Location[]>(this.locationsBaseUri, {params: parameters});
  }

  /**
   * Returns a list of the persisting in the data base countries
   */
  public getCountriesOrderedByName(): Observable<string[]> {
    console.log('Location Service: getCountriesOrderedByName');
    return this.httpClient.get<string[]>(this.locationsBaseUri + '/countries');
  }

  /**
   * gets search suggestions for location dto as needed in Floorplan component
   * @param name substring of names of all locations found
   */
  public getSearchSuggestions(name: string): Observable<Location[]> {
    console.log('Location Service: Getting search suggestions for location name entered ' + name);
    return this.httpClient.get<Location[]>(this.locationsBaseUri + '/suggestions', {params: new HttpParams().set('name', name)});
  }

  /**
   * Finds one Location by id
   * @param id of the location to look for
   */
  findOneById(id: number): Observable<Location> {
    return this.httpClient.get<Location>(this.locationsBaseUri + '/' + id);
  }
}
