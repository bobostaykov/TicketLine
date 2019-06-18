import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Artist} from '../../../dtos/artist';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ArtistResultsService {

  private artistBaseUri: string = this.globals.backendUri + '/artists';

  constructor(private httpClient: HttpClient, private globals: Globals) { }
  
  /**
   * Get all artists whose name contains the string 'artistName' from the backend
   *
   * @param artistName to look for
   * @param page the number of the requested page
   */
  public findArtists(artistName, page): Observable<Artist[]> {
    return this.httpClient.get<Artist[]>(this.artistBaseUri, {params: { artist_name: artistName, page: page }});
  }

}
