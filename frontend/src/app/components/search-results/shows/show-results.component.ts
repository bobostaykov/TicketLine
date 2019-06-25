import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Show} from '../../../dtos/show';
import {ResultsFor} from '../../../datatype/results_for';
import {ShowResultsService} from '../../../services/search-results/shows/show-results.service';
import {Event} from '../../../dtos/event';
import {Hall} from '../../../dtos/hall';

@Component({
  selector: 'app-shows',
  templateUrl: './show-results.component.html',
  styleUrls: ['./show-results.component.scss']
})
export class ShowResultsComponent implements OnInit {

  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  private minPrice: number;
  private maxPrice: number;
  private duration: number;
  private eventName: string;
  private eventType: string;
  private artistName: string;
  private hallName: string;
  private dateFrom: string;
  private dateTo: string;
  private timeFrom: string;
  private timeTo: string;
  private locationId: number;
  private locationName: string;
  private country: string;
  private city: string;
  private street: string;
  private postalCode: string;

  private noResultsFound: boolean = false;
  private error: boolean = false;
  private errorMessage: string = '';

  private resultsFor: string;

  private shows: Show[];
  private headElements: string[] = [
    'Event',
    'Type',
    'Location',
    'Date',
    'Artist',
    'Hall',
    'Description',
    'Sold Tickets',
    'Buy'
  ];

  constructor(private route: ActivatedRoute, private showService: ShowResultsService) { }

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    console.log(this.resultsFor);
    switch (this.resultsFor) {
      case 'EVENT':
        this.loadShowsFilteredByEventName( this.eventName = this.route.snapshot.queryParamMap.get('name'), this.page);
        break;
      case 'LOCATION':
        this.loadShowsFilteredByLocationID( this.locationId = +this.route.snapshot.queryParamMap.get('id'), this.page);
        break;
      case 'ATTRIBUTES':
        this.loadShowsFilteredSomeParameters(
          this.eventName = this.route.snapshot.queryParamMap.get('eventName'),
          this.eventType = this.route.snapshot.queryParamMap.get('eventType'),
          this.artistName = this.route.snapshot.queryParamMap.get('artistName'),
          this.hallName = this.route.snapshot.queryParamMap.get('hallName'),
          this.dateFrom = this.route.snapshot.queryParamMap.get('dateFrom'),
          this.dateTo = this.route.snapshot.queryParamMap.get('dateTo'),
          this.timeFrom = this.route.snapshot.queryParamMap.get('timeFrom'),
          this.timeTo = this.route.snapshot.queryParamMap.get('timeTo'),
          this.minPrice = +this.route.snapshot.queryParamMap.get('minPrice'),
          this.maxPrice = +this.route.snapshot.queryParamMap.get('maxPrice'),
          this.duration = +this.route.snapshot.queryParamMap.get('duration'),
          this.locationName = this.route.snapshot.queryParamMap.get('locationName'),
          this.country = this.route.snapshot.queryParamMap.get('country'),
          this.city = this.route.snapshot.queryParamMap.get('city'),
          this.street = this.route.snapshot.queryParamMap.get('street'),
          this.postalCode = this.route.snapshot.queryParamMap.get('postalCode'),
          this.page
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private loadShows() {
    switch (this.resultsFor) {
      case 'EVENT': this.loadShowsFilteredByEventName(this.eventName, this.page);
        break;
      case 'LOCATION': this.loadShowsFilteredByLocationID(this.locationId, this.page);
        break;
      case 'ATTRIBUTES': this.loadShowsFilteredSomeParameters(
        this.eventName, this.eventType, this.artistName, this.hallName, this.dateFrom, this.dateTo, this.timeFrom,
        this.timeTo, this.minPrice, this.maxPrice, this.duration, this.locationName, this.country,
        this.city, this.street, this.postalCode, this.page);
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
    this.loadShows();
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event to handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadShows();
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
      this.loadShows();
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

  private loadShowsFilteredByEventName(eventName, page: number) {
    console.log('ShowResultsComponent: loadShowsFilteredByEventName');
    this.showService.findShowsFilteredByEventName(eventName, page).subscribe(
      result => {
        this.shows = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private loadShowsFilteredByLocationID(id: number, page: number) {
    console.log('ShowResultsComponent: loadShowsFilteredByLocationID');
    this.showService.findShowsFilteredByLocationID(id, page).subscribe(
      result => {
        this.shows = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();

      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private loadShowsFilteredSomeParameters(eventName, eventType, artistName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice: number, maxPrice: number,
                                            duration: number, locationName, country, city, street, postalCode, page: number) {
    console.log('ShowResultsComponent: loadShowsFilteredSomeParameters');
    this.showService.findShowsFilteredBySomeParameters(eventName, eventType, artistName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice,
      duration, locationName, country, city, street, postalCode, page).subscribe(
      result => {
        this.shows = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
        },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
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
