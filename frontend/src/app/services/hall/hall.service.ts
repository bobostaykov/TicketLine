import { Injectable } from '@angular/core';
import { Globals } from '../../global/globals';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Hall} from '../../dtos/hall';
import {HallRequestParameter} from '../../datatype/requestParameters/HallRequestParameter';

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
    console.log('Add hall to backend ' + hall.toString());
    return this.httpClient.post<Hall>(this.hallBaseUri, hall);
  }

  /**
   * searches for and loads hall from backend
   * @param hallName search parameter. If set only halls with names containing this parameter are found
   * @param hallLocation search parameter. If set only halls with this location are found
   * @param fields contain special instructions on which fields are to be included with found halls
   * For example if fields is set to id, only hall ids are found and returned from backend
   */
  searchHalls(hallName: string, hallLocation: Location, fields: HallRequestParameter[]): Observable<Hall[]> {
    console.log('Getting all halls from search parameters: name = ' + hallName + ', location = ' + JSON.stringify(hallLocation) +
      ' with ' + (fields ? ' fields ' + JSON.stringify(fields) : ' all fields'));
    let parameters = new HttpParams();
    parameters = fields ? parameters.append('fields', fields.toString()) : parameters;
    parameters = hallName ? parameters.append('name', hallName) : parameters;
    return this.httpClient.get<Hall[]>(this.hallBaseUri, {params: parameters});
  }

  /**
   * finds and loads a single hall entity by its id
   * @param id of hall to be found
   * @param include includes special request parameters
   * if set to 'editing' hall entities will include boolean declaring whether edits are allowed
   */
  findOneById(id: number, include: HallRequestParameter[]): Observable<Hall> {
    console.log('Get hall with id ' + id + ' and request parameters ' + include.toString());
    const parameters = include ? new HttpParams().set('include', include.toString()) : null;
    return this.httpClient.get<Hall>(this.hallBaseUri + '/' + id, {params: parameters});
  }

  /**
   * updates hall passed as parameter
   * @param hall entity to be updated with new parameters
   */
  updateHall(hall: Hall): Observable<Hall> {
    console.log('Update hall with id ' + hall.id + ' to ' + hall.toString());
    return this.httpClient.put<Hall>(this.hallBaseUri, hall);
  }

  /**
   * deletes hall with given id
   * @param id of hall to be deleted
   */
  deleteHall(id: number):  Observable<{}>  {
    console.log('Deleting hall with id ' + id);
    return this.httpClient.delete(this.hallBaseUri + '/' + id);
  }
}
