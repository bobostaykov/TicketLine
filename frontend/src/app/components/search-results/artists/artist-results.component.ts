import { Component, OnInit } from '@angular/core';
import {Artist} from '../../../dtos/artist';
import {ArtistResultsService} from '../../../services/search-results/artists/artist-results.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-artist-results',
  templateUrl: './artist-results.component.html',
  styleUrls: ['./artist-results.component.scss']
})
export class ArtistResultsComponent implements OnInit {

  private page: number = 0;
  private pages: Array<number>;
  private dataReady: boolean = false;

  private error: boolean = false;
  private errorMessage: string = '';

  private artists: Artist[] = [];
  private artistName: string;

  constructor(private artistResultsService: ArtistResultsService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.artistName = this.route.snapshot.queryParamMap.get('artist_name');
    this.loadArtists();
  }

  private loadArtists() {
    this.artistResultsService.findArtists(this.artistName, this.page, null).subscribe(
      result => {
        this.artists = result['content'];
        this.pages = new Array(result['totalPages']);
      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadArtists();
  }

  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadArtists();
    }
  }

  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.pages.length - 1) {
      this.page++;
      this.loadArtists();
    }
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.error;
    }
  }

}
