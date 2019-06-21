import { Injectable } from '@angular/core';
import { Globals } from '../../global/globals';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Hall} from '../../dtos/hall';
import {HallRequestParameter} from '../../datatype/HallRequestParameter';

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

  /**
   * persists hall to the backend
   * @param hall to be added to the backend
   */
  createHall(hall: Hall) {
    console.log('Create hall with name ' + hall.name);
    return this.httpClient.post<Hall>(this.hallBaseUri, hall);
  }

  searchHalls(hallName: string, hallLocation: Location, fields: HallRequestParameter[]): Observable<Hall[]> {
    console.log('Getting all halls from search parameters: name = ' + hallName + ', location = ' + JSON.stringify(hallLocation) +
      ' with ' + (fields ? ' fields ' + JSON.stringify(fields) : ' all fields'));
    let parameters = new HttpParams();
    parameters = fields ? parameters.append('fields', fields.toString()) : parameters;
    parameters = hallName ? parameters.append('name', hallName) : parameters;
    return this.httpClient.get<Hall[]>(this.hallBaseUri, {params: parameters});
  }

  findOneById(id: number): Observable<Hall> {
    console.log('Get hall with id ' + id);
    return this.httpClient.get<Hall>(this.hallBaseUri + '/' + id);
  }
}
