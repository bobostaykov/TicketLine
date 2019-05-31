import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {EventType} from '../../../datatype/event_type';
import {Time} from '@angular/common';
import { Options } from 'ng5-slider';
import {MatSnackBar} from '@angular/material';
import {LocationsService} from '../../../services/search-results/locations/locations.service';

@Component({
  selector: 'app-search-events',
  templateUrl: './search-events.component.html',
  styleUrls: ['./search-events.component.scss']
})
export class SearchEventsComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';

  // Search for artists
  private artistName: string = '';
  private artistForm: FormGroup;

  // Search for locations
  private country: string = '';
  private city: string = '';
  private street: string = '';
  private postalCode: string = '';
  private locationDescription: string = '';
  private countries: string[];
  private locationForm: FormGroup;

  // Search events by name and type
  private eventName: string = '';
  private eventContent: string = '';
  private eventDescription: string = '';
  private eventType: EventType;
  private eventTypeKeys: string[] = Object.keys(EventType);
  private eventForm: FormGroup;

  // Search shows by specific attributes
  private dateFrom: Date;
  private dateTo: Date;
  private timeFrom: Time;
  private timeTo: Time;
  private minPrice: number = 40;
  private maxPrice: number = 160;
  private priceOptions: Options = {
    floor: 0,
    ceil: 200,
    translate: (value: number): string => {
      return '€' + value;
    }
  };
  private duration: number;
  private eventName2: string = '';
  private hallName: string = '';
  private showForm: FormGroup;

  constructor(private router: Router, private locationsService: LocationsService, public snackBar: MatSnackBar) { }

  ngOnInit() {

    this.artistForm = new FormGroup({
      artist_name: new FormControl('', [Validators.required, Validators.maxLength(60)])
    });

    this.locationForm = new FormGroup({
      country: new FormControl(),
      city: new FormControl(),
      street  : new FormControl(),
      postalCode: new FormControl(),
      locationDescription: new FormControl()
      });

    this.eventForm = new FormGroup({
      eventName: new FormControl(),
      eventContent: new FormControl(),
      eventDescription: new FormControl(),
      eventType: new FormControl()
    });

    this.showForm = new FormGroup({
      eventName2: new FormControl(),
      hallName: new FormControl(),
      minPrice: new FormControl(),
      maxPrice: new FormControl(),
      dateFrom: new FormControl(),
      dateTo: new FormControl(),
      timeFrom: new FormControl(),
      timeTo: new FormControl(),
      duration: new FormControl()
    });

    this.getCountriesOrderedByName();
  }

  private getCountriesOrderedByName() {
      this.locationsService.getCountriesOrderedByName().subscribe(
        (countries: string[]) => { this.countries = countries; },
        error => { this.defaultServiceErrorHandling(error); }
      );
  }

  private submitArtistForm() {
    if (this.artistName !== '') {
      this.router.navigate(['/events/search/results/artists'], { queryParams: { artist_name: this.artistName } });
    }
  }

  private searchByLocation(): void {
    if (this.country !== '' || this.city !== '' || this.street !== '' || this.postalCode !== '' || this.postalCode !== '' || this.locationDescription !== '') {
      this.router.navigate(['events/search/results/locations'], {
        queryParams: {
          resultsFor: 'ATTRIBUTES', country: this.country, city: this.city, street: this.street,
          postalCode: this.postalCode, description: this.locationDescription
        }
      });
    }
  }

  private searchByEventAttributes(): void {
    if (this.eventName !== '' || this.eventContent !== '' || this.eventDescription !== '' || this.eventType !== null) {
      this.router.navigate(['/events/search/results/events'], {
        queryParams: {
          resultsFor: 'ATTRIBUTES', eventName: this.eventName, eventType: this.eventType, content: this.eventContent, description: this.eventDescription
        }
      });
    }
  }

  private searchForSpecificShows(): void {

    if (this.dateFrom > this.dateTo) {
      this.openSnackBar('Invalid Dates: \"From Date\" is after than \"To Date\"');
      return;
    }

    if (this.timeFrom > this.timeTo) {
      this.openSnackBar('Invalid Times: \"From Time\" is after than \"To Time\"');
      return;
    }

    if (this.duration < 0) {
      this.openSnackBar('Invalid Duration: Duration cannot be negative');
      return;
    }

    if (this.eventName2 !== '' || this.hallName !== '' || this.dateFrom !== undefined || this.dateTo !== undefined || this.timeFrom !== undefined ||
      this.timeTo !== undefined || this.minPrice !== null || this.maxPrice !== null || this.duration !== null) {
      this.router.navigate(['/events/search/results/shows'], {
        queryParams: {
          resultsFor: 'ATTRIBUTES', eventName: this.eventName2, hallName: this.hallName, dateFrom: this.dateFrom,
          dateTo: this.dateTo, timeFrom: this.timeFrom, timeTo: this.timeTo, minPrice: this.minPrice,
          maxPrice: this.maxPrice, duration: this.duration
        }
      });
    }
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, null, {duration: 3000});
  }

  // TODO not a universal method
  public hasError = (controlName: string, errorName: string) => {
    return this.artistForm.controls[controlName].hasError(errorName);
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.news !== 'No message available') {
      this.errorMessage = error.error.news;
    } else {
      this.errorMessage = error.error.error;
    }
  }
}
