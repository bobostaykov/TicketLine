import {Component, OnInit} from '@angular/core';
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
import {EventService} from '../../services/event/event.service';
import {EventDialogComponent} from '../event-dialog/event-dialog.component';

@Component({
  selector: 'app-content-manager',
  templateUrl: './content-manager.component.html',
  styleUrls: ['./content-manager.component.scss'],
  providers: [EventDialogComponent]
})
export class ContentManagerComponent implements OnInit {

  private savedType: string;
  private savedName: string;

  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  private noResultsFound: boolean = false;
  private error: boolean = false;
  private errorMessage: string = '';

  // Add Content
  private addType: string;
  private addForm: FormGroup;

  // Search Content
  private searchName: string;
  private searchType: string;
  private searchForm: FormGroup;

  // Add/Update Event
  private activeEvent: Event;

  // Add/Update Location
  private activeLocation: Location;

  // Add/Update Artist
  private artistToAdd: Artist = {id: undefined, name: undefined};
  private artistToUpdate: Artist;
  private artistToDelete: number;
  private addArtistForm: FormGroup;
  private updateArtistForm: FormGroup;

  // Add/Update Show
  private showToDelete: number;
  private showToUpdate: Show;

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
              private showService: ShowResultsService, private eventResultsService: EventResultsService,
              private locationService: LocationResultsService, private eventService: EventService,
              private eventDialog: EventDialogComponent) { }

  ngOnInit() {
    this.addForm = new FormGroup({
      addType: new FormControl()
    });

    this.searchForm = new FormGroup({
      searchName: new FormControl(),
      searchType: new FormControl()
    });

    this.updateArtistForm = new FormGroup({
      updateArtistNameControl: new FormControl('', [Validators.required, Validators.maxLength(60)])
    });

    this.addArtistForm = new FormGroup({
        addArtistNameControl: new FormControl('', [Validators.required, Validators.maxLength(60)])
    });
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  private isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * Reset page number and calls loadEntities()
   */
  private searchContent(): void {
    console.log('ContentManager: searchContent');
    this.noResultsFound = false;
    if (this.searchName === undefined || this.searchType === undefined) {
      this.openSnackBar('Both fields must be filled!', 'red-snackbar');
      return;
    }

    this.savedType = this.searchType;
    this.savedName = this.searchName;

    this.page = 0;
    this.loadEntities();
  }

  /**
   * Loads entities for the 'savedName' and 'savedType'
   */
  private loadEntities() {
    switch (this.savedType) {
      case 'Artist':
        this.artistService.findArtists(this.savedName, this.page).subscribe(
          result => {
            this.artists = result['content'];
            this.totalPages = result['totalPages'];
            this.setPagesRange();
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
        );
        break;
      case 'Show':
        this.showService.findShowsFilteredByEventName(this.savedName, this.page).subscribe(
          result => {
            this.shows = result['content'];
            this.totalPages = result['totalPages'];
            this.setPagesRange();
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
        );
        break;
      case 'Event':
        this.eventResultsService.findEventsFilteredByAttributes(this.savedName, null, null, null, null, this.page).subscribe(
          result => {
            this.events = result['content'];
            this.totalPages = result['totalPages'];
            this.setPagesRange();
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; console.log(this.events.length); console.log(this.events.toString()); }
        );
        break;
      case 'Location':
        this.locationService.findLocationsFiltered(this.savedName, null, null, null, null, null, this.page).subscribe(
          result => {
            this.locations = result['content'];
            this.totalPages = result['totalPages'];
            this.setPagesRange();
          },
          error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadEntities();
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event to handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadEntities();
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
      this.loadEntities();
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

  private setShowToDelete(showId: number) {
    this.showToDelete = showId;
  }

  private setArtistToDelete(artistId: number) {
    this.artistToDelete = artistId;
  }

  private setShowToUpdate(show: Show) {
    this.showToUpdate = Object.assign({}, show);
  }

  private setArtistToUpdate(artist: Artist) {
    this.artistToUpdate = Object.assign({}, artist);
  }

  private setActiveLocation(location: Location) {
    this.activeLocation = location;
  }

  private updateLocation(location: Location) {
    console.log('ContentManager: updateLocation:' + JSON.stringify(location));
    this.locationService.updateLocation(location).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEntities(); }
    );
  }

  private addLocation(location: Location) {
    console.log('ContentManager: addLocation: ' + JSON.stringify(location));
    this.locationService.addLocation(location).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEntities(); }
    );
  }

  private deleteLocation() {
    console.log('Delete location with id ' + this.activeLocation.id);
    this.locationService.deleteLocation(this.activeLocation.id).subscribe(
      () => {},
      error => this.defaultServiceErrorHandling(error),
      () => { this.savedName = ''; this.loadEntities(); }
    );
  }

  private updateShow(show: Show) {
    console.log('ContentManager: updateShow:' + JSON.stringify(show));
    // Object.assign(this.showToUpdate, show);
    this.showService.updateShow(show).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEntities(); }
    );
  }

  private addShow(show: Show) {
    console.log('ContentManager: addShow: ' + JSON.stringify(show));
    this.showService.addShow(show).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEntities(); }
    );
  }

  private deleteShow() {
    console.log('ContentManager: deleteShow');
    this.showService.deleteShow(this.showToDelete).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.showToDelete = null; this.loadEntities(); }
    );
  }

  private addArtist() {
    console.log('ContentManager: addArtist');
    this.artistService.addArtist(this.artistToAdd).subscribe(
      () => { this.addArtistForm.reset(); },
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEntities();  }
    );
  }

  private updateArtist() {
    console.log('ContentManager: updateArtist');
    this.artistService.updateArtist(this.artistToUpdate).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEntities(); }
    );
  }

  private deleteArtist() {
    console.log('ContentManager: deleteArtist');
    this.artistService.deleteArtist(this.artistToDelete).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.artistToDelete = null; this.loadEntities(); }
    );
  }

  private addEvent(event: Event) {
    this.eventService.createEvent(event).subscribe(
      () => {},
      error => this.defaultServiceErrorHandling(error),
      () => window.location.reload()
    );
  }

  private setActiveEvent(event: Event) {
    this.activeEvent = event;
  }

  private updateEvent(event: Event) {
    console.log('Updates event with id ' + event.id + ' to ' + JSON.stringify(event));
    Object.assign(this.activeEvent, event);
    this.eventService.updateEvent(event).subscribe(
      () => {},
      error => this.defaultServiceErrorHandling(error),
      () => this.loadEntities()
    );
  }

  private deleteEvent() {
    console.log('Delete event with id ' + this.activeEvent.id);
    this.eventService.deleteEvent(this.activeEvent.id).subscribe(
      () => {},
      error => this.defaultServiceErrorHandling(error),
      () => { this.savedName = ''; this.loadEntities(); }
    );
  }

  openSnackBar(message: string, css: string) {
    this.snackBar.open(message, '', {duration: 3000, panelClass: [css]});
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
