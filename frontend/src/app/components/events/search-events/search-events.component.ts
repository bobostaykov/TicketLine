import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {EventType} from '../../../datatype/event_type';
import {Time} from '@angular/common';
import { Options } from 'ng5-slider';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-search-events',
  templateUrl: './search-events.component.html',
  styleUrls: ['./search-events.component.scss']
})
export class SearchEventsComponent implements OnInit {

  private artistName: string = '';
  private artistForm: FormGroup;

  private country: string = '';
  private city: string = '';
  private street: string = '';
  private postalCode: string = '';
  private locationDescription: string = '';
  // Should the options be listed here
  private countries: string[] = ['Austria', 'Germany', 'France', 'Spain', 'Great Britain', 'Bulgaria', 'Italy', 'Russia', 'USA', 'Mexico'];
  private locationForm: FormGroup;

  private eventName: string = '';
  private eventContent: string = '';
  private eventDescription: string = '';
  private eventType: EventType;
  private eventTypeKeys: string[] = Object.keys(EventType);

  private eventForm: FormGroup;

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

  constructor(private router: Router, public snackBar: MatSnackBar) { }

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
    console.log('Search Events Component: searchForSpecificShows');
    if (this.dateFrom > this.dateTo) {
      this.openSnackBar('Invalid Dates: \"From Date\" is after than \"To Date\"');
      console.log('Invalid dates');
    } else if (this.timeFrom > this.timeTo) {
      this.openSnackBar('Invalid Times: \"From Time\" is after than \"To Time\"');
      console.log('Invalid times');
    } else {
      if (this.eventName2 !== '' || this.hallName !== '' || this.dateFrom !== null || this.dateTo !== null || this.timeFrom !== null ||
        this.timeTo !== null || this.minPrice !== null || this.maxPrice !== null || this.duration !== null) {
        this.router.navigate(['/events/search/results/shows'], {
          queryParams: {
            resultsFor: 'ATTRIBUTES', eventName: this.eventName2, hallName: this.hallName, dateFrom: this.dateFrom,
            dateTo: this.dateTo, timeFrom: this.timeFrom, timeTo: this.timeTo, minPrice: this.minPrice,
            maxPrice: this.maxPrice, duration: this.duration
          }
        });
      }
    }
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Undo', {duration: 3000});
  }

  // TODO not a universal method
  public hasError = (controlName: string, errorName: string) => {
    return this.artistForm.controls[controlName].hasError(errorName);
  }
}
