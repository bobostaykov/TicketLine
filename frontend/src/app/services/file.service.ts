import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private fileBaseUri: string = this.globals.backendUri + '/files';


  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads specific file from the backend
   * @param name of file to load
   */
  getFile(name: string): Observable<File> {
    console.log('Load file with name ' + name);
    return this.httpClient.get<File>(this.fileBaseUri + '/' + name);
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
    //this.httpClient.post(this.fileBaseUri, formData, { responseType: 'text'}).subscribe(val =>  {valString = val});
    returnValue = this.httpClient.post(this.fileBaseUri, formData, { responseType : 'text'});
    return returnValue;
    //console.log('image is: ' + returnValue);

    //return returnValue;
  }
}
