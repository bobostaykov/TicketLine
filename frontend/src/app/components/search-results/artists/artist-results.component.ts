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
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  private noResultsFound: boolean = false;
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
    this.artistResultsService.findArtists(this.artistName, this.page).subscribe(
      result => {
        this.artists = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }


  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadArtists();
  }


  /**
   * Sets page number to the previous one and calls the last method
   * @param event to handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadArtists();
    }
  }

  /**
   * Sets page number to the next one and calls the last method
   * @param event to handle
   */
  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadArtists();
    }
  }

  /**
   * Determines the page numbers which will be shown in the clickable menu
   */
  private setPagesRange() {
    this.pageRange = []; // nullifies the array
    if (this.totalPages <= 11) {
      for (let i = 0; i < this.totalPages; i++) {
        this.pageRange.push(i);
      }
    } else {
      if (this.page <= 5) {
        for (let i = 0; i <= 10; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page > 5 && this.page < this.totalPages - 5) {
        for (let i = this.page - 5; i <= this.page + 5; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page >= this.totalPages - 5) {
        for (let i = this.totalPages - 10; i < this.totalPages; i++) {
          this.pageRange.push(i);
        }
      }
    }
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.status === 404) {
      console.log('No results found!');
      this.noResultsFound = true;
    }
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.error;
    }
  }

}
