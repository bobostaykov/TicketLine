import { Injectable } from '@angular/core';
import {ResultsFor} from '../../datatype/results_for';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {Show} from '../../dtos/show';

@Injectable({
  providedIn: 'root'
})
export class ShowService {

  private showBaseUri: string = this.globals.backendUri + '/shows';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Get all shows that correspond to a particular type of filter (resultsFor: LOCATION, ARTIST, EVENT), with a specific name or id
   * If resultsFor === ResultsFor.LOCATION, name_or_id will contain the id of the location, otherwise the name of the artist/event
   */
  public findShows(resultsFor: ResultsFor, nameOrId: string): Observable<Show[]> {
    console.log('Find shows for', resultsFor, nameOrId);
    return this.httpClient.get<Show[]>(this.showBaseUri, {params: { results_for: ResultsFor[resultsFor], name_or_id: nameOrId }});
  }

  public findShowsFilteredByEventName(eventName: string): Observable<Show[]> {
    console.log('ShowService: findShowsFilteredByEventName');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/event',{params: {eventName: eventName}});
  }

  public findShowsFilteredByLocationID(id: number): Observable<Show[]> {
    console.log('ShowService: findShowsFilteredByLocationID');
    console.log('test: ' + '/location/${id}');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/location/${id}');
  }

  public findShowsFilteredByLocation(country: string, city: string, street: string, postalCode: string, description: string) {
    console.log('ShowService: findShowsFilteredByLocation');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/location', {params: {country: country, city: city, postalCode: postalCode, description: description}});
  }

  public findShowsFilteredByShowAttributes() {
    console.log('ShowService: findShowsFilteredByShowAttributes');
  }
}
