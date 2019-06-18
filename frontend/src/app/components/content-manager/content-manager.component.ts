import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Event} from '../../dtos/event';
import {Artist} from '../../dtos/artist';
import {Show} from '../../dtos/show';
import {ShowResultsService} from '../../services/search-results/shows/show-results.service';
import {ArtistResultsService} from '../../services/search-results/artists/artist-results.service';
import {LocationResultsService} from '../../services/search-results/locations/location-results.service';
import {EventResultsService} from '../../services/search-results/events/event-results.service';
import {Location} from '../../dtos/location';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-content-manager',
  templateUrl: './content-manager.component.html',
  styleUrls: ['./content-manager.component.scss']
})
export class ContentManagerComponent implements OnInit {

  private savedType: string;

  private page: number = 0;
  private pages: Array<number>;
  private dataReady: boolean = false;

  private error: boolean = false;
  private errorMessage: string = '';

  // Add Content
  private addType: string;
  private addForm: FormGroup;

  // Search Content
  private searchName: string;
  private searchType: string;
  private searchForm: FormGroup;

  private artists: Artist[];
  private shows: Show[];
  private events: Event[];
  private locations: Location[];

  private locationHeaders: string[] = [
    'Name',
    'Country',
    'City',
    'Postcode',
    'Street',
    'Description',
    'Edit',
    'Remove'
  ];

  private artistHeaders: string[] = [
    'Name',
    'Edit',
    'Remove'
  ];

  private showHeaders: string[] = [
    'Event',
    'Date/Time',
    'Hall',
    'Description',
    'TicketsSold',
    'Edit',
    'Remove'
  ];

  private eventHeaders: string[] = [
    'Name',
    'Type',
    'Artist',
    'Duration',
    'Description',
    'Content',
    'Edit',
    'Remove'
  ];

  constructor(private authService: AuthService, public snackBar: MatSnackBar, private artistService: ArtistResultsService,
              private showService: ShowResultsService, private eventService: EventResultsService, private locationService: LocationResultsService) { }

  ngOnInit() {
    this.addForm = new FormGroup({
      addType: new FormControl()
    });

    this.searchForm = new FormGroup({
      searchName: new FormControl(),
      searchType: new FormControl()
    });
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  private isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  private addContent(): void {
    if (this.addType === undefined) {
      this.openSnackBar('Type is required!', 'red-snackbar');
      return;
    }
    console.log('ContentManager: addContent');
    switch (this.addType) {
      case 'Artist':
        break;
      case 'Show':
        break;
      case 'Event':
        break;
      case 'Location':
        break;
      default:
        this.defaultServiceErrorHandling('Can\'t create an object from this type');
    }
  }

  private searchContent(): void {
    console.log('ContentManager: searchContent');
    if (this.searchName === undefined || this.searchType === undefined) {
      this.openSnackBar('Both fields must be filled!', 'red-snackbar');
      return;
    }

    this.savedType = this.searchType; // used to save the type so that the tables in the html file doesn't change when changing the search mask
    switch (this.searchType) {
      case 'Artist':
        this.artistService.findArtists(this.searchName, this.page).subscribe(
          result => {
            this.artists = result['content'];
            this.pages = new Array(result['totalPages']);
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
        );
        break;
      case 'Show':
        this.showService.findShowsFilteredByEventName(this.searchName, this.page).subscribe(
          result => {
            this.shows = result['content'];
            this.pages = new Array(result['totalPages']);
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
        );
        break;
      case 'Event':
        this.eventService.findEventsFilteredByAttributes(this.searchName, null, null, null, null, this.page).subscribe(
          result => {
            this.events = result['content'];
            this.pages = new Array(result['totalPages']);
            console.log(result);
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; console.log(this.events.length); console.log(this.events.toString()); }
        );
        break;
      case 'Location':
        this.locationService.findLocationsFiltered(this.searchName, null, null, null, null, null, this.page).subscribe(
          result => {
            this.locations = result['content'];
            this.pages = new Array(result['totalPages']);
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.searchContent();
  }

  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.searchContent();
    }
  }

  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.pages.length - 1) {
      this.page++;
      this.searchContent();
    }
  }

  openSnackBar(message: string, css: string) {
    this.snackBar.open(message, '', {duration: 3000, panelClass: [css]});
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
