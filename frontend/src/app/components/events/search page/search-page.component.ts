import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {EventType} from '../../../datatype/event_type';
import {Time} from '@angular/common';
import { Options } from 'ng5-slider';
import {MatSnackBar} from '@angular/material';
import {LocationResultsService} from '../../../services/search-results/locations/location-results.service';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.scss']
})
export class SearchPageComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';

  // Search for artists
  private artistName: string = '';
  private artistForm: FormGroup;

  // Search for locations
  private countries: string[];
  private locationName: string;
  private locationCountry: string;
  private locationCity: string;
  private locationStreet: string;
  private locationPostalCode: string;
  private locationDescription: string;
  private locationForm: FormGroup;

  // Search for events
  private eventName: string;
  private eventContent: string;
  private eventDescription: string;
  private eventArtistName: string;
  private eventType: EventType;
  private eventTypeKeys: string[] = Object.keys(EventType);
  private eventForm: FormGroup;

  // Search for shows
  private dateFrom: Date;
  private dateTo: Date;
  private timeFrom: Time;
  private timeTo: Time;
  private priceOptions: Options = {
    floor: 0,
    ceil: 200,
    translate: (value: number): string => '€' + value
  };
  private minPrice: number = this.priceOptions.floor;
  private maxPrice: number = this.priceOptions.floor;
  private duration: number;
  private hallName: string;
  private showArtistName: string;
  private showEventName: string;
  private showEventType: EventType;
  private showLocationName: string;
  private showCountry: string;
  private showCity: string;
  private showStreet: string;
  private showPostalCode: string;
  private showForm: FormGroup;

  constructor(private router: Router, private locationsService: LocationResultsService, public snackBar: MatSnackBar) { }

  ngOnInit() {

    this.artistForm = new FormGroup({
      artist_name: new FormControl('', [Validators.required, Validators.maxLength(60)])
    });

    this.locationForm = new FormGroup({
      locationName: new FormControl(),
      locationCountry: new FormControl(),
      locationCity: new FormControl(),
      locationStreet  : new FormControl(),
      locationPostalCode: new FormControl(),
      locationDescription: new FormControl()
      });

    this.eventForm = new FormGroup({
      eventName: new FormControl(),
      eventContent: new FormControl(),
      eventDescription: new FormControl(),
      eventType: new FormControl(),
      eventArtistName: new FormControl()
    });

    this.showForm = new FormGroup({
      showEventName: new FormControl(),
      showEventType: new FormControl(),
      showArtistName: new FormControl(),
      hallName: new FormControl(),
      minPrice: new FormControl(),
      maxPrice: new FormControl(),
      dateFrom: new FormControl(),
      dateTo: new FormControl(),
      timeFrom: new FormControl(),
      timeTo: new FormControl(),
      duration: new FormControl(),
      showLocationName: new FormControl(),
      showCountry: new FormControl(),
      showCity: new FormControl(),
      showStreet: new FormControl(),
      showPostalCode: new FormControl()
    });

    this.getCountriesOrderedByName();
  }

  /**
   * Returns a list of the names of the countries available in the data base
   */
  private getCountriesOrderedByName() {
      this.locationsService.getCountriesOrderedByName().subscribe(
        (countries: string[]) => { this.countries = countries; },
        error => { this.defaultServiceErrorHandling(error); }
      );
  }

  private searchForArtists() {
    if (this.artistName !== '') {
      this.router.navigate(['/events/search/results/artists'], { queryParams: { artist_name: this.artistName } });
    }
  }

  private searchForLocations(): void {
    if (this.locationName !== undefined || this.locationCountry !== undefined || this.locationCity !== undefined || this.locationStreet !== undefined ||
      this.locationPostalCode !== undefined || this.locationDescription !== undefined ) {
      this.router.navigate(['events/search/results/locations'], {
        queryParams: {
          resultsFor: 'ATTRIBUTES', name: this.locationName, country: this.locationCountry, city: this.locationCity, street: this.locationStreet,
          postalCode: this.locationPostalCode, description: this.locationDescription
        }
      });
    }
  }

  private searchForEvents(): void {
    if (this.eventArtistName !== undefined || this.eventName !== undefined || this.eventContent !== undefined || this.eventDescription !== undefined || this.eventType !== undefined) {
      this.router.navigate(['/events/search/results/events'], {
        queryParams: {
          resultsFor: 'ATTRIBUTES', eventName: this.eventName, eventType: this.eventType, artistName: this.eventArtistName, content: this.eventContent, description: this.eventDescription
        }
      });
    }
  }

  private searchForShows(): void {

    if (this.dateFrom > this.dateTo) {
      this.openSnackBar('Invalid Dates: \"From Date\" is after than \"To Date\"');
      return;
    }

    if (this.timeFrom > this.timeTo) {
      this.openSnackBar('Invalid Times: \"From Time\" is after than \"To Time\"');
      return;
    }

    if (this.duration <= 0) {
      this.openSnackBar('Invalid Duration: Duration must be positive');
      return;
    }

    if ( this.dateFrom !== undefined || this.dateTo !== undefined || this.timeFrom !== undefined || this.timeTo !== undefined ||
    this.minPrice !== 0 || this.maxPrice !== 0 || this.duration !== undefined || this.hallName !== undefined ||
    this.showArtistName !== undefined || this.showEventName !== undefined || this.showEventType !== undefined || this.showLocationName !== undefined ||
      this.showCountry !== undefined || this.showCity !== undefined || this.showStreet !== undefined || this.showPostalCode !== undefined ) {
      this.router.navigate(['/events/search/results/shows'], {
        queryParams: {
          resultsFor: 'ATTRIBUTES', eventName: this.showEventName, eventType: this.showEventType, artistName: this.showArtistName, hallName: this.hallName, dateFrom: this.dateFrom,
          dateTo: this.dateTo, timeFrom: this.timeFrom, timeTo: this.timeTo, minPrice: this.minPrice, maxPrice: this.maxPrice,
          duration: this.duration, locationName: this.showLocationName, country: this.showCountry, city: this.showCity, street: this.showStreet, postalCode: this.showPostalCode
        }
      });
    }
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, null, {duration: 3000, panelClass: ['red-snackbar']});
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.artistForm.controls[controlName].hasError(errorName);
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
