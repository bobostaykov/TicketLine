import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private fileBaseUri: string = this.globals.backendUri + '/news/file';


  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads specific file from the backend
   * @param name of file to load
   */
  getFile(id: string): Observable<Blob> {
    console.log('Load file with id ' + id);
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    return this.httpClient.get<Blob>(this.fileBaseUri + '/' + id, {headers: headers, responseType: 'blob' as 'json'});
  }

  /**
   * Persists file to the backend
   * @param file to persist
   */
  uploadFile(file: File): Observable<string> {
    console.log('Upload file with name ' + file.name);
    const formData = new FormData();
    formData.append('file', file);
    let returnValue: Observable<string>;
    returnValue = this.httpClient.post(this.fileBaseUri, formData, { responseType : 'text'});
    return returnValue;
  }
}
