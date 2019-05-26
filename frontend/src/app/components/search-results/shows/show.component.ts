import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Show} from '../../../dtos/show';
import {ResultsFor} from '../../../datatype/results_for';
import {ShowService} from '../../../services/search-results/shows/show.service';
import {Event} from '../../../dtos/event';
import {Hall} from '../../../dtos/hall';

@Component({
  selector: 'app-shows',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.scss']
})
export class ShowComponent implements OnInit {

  private minPrice: number;
  private maxPrice: number;
  private duration: number;

  private page: number = 1;
  private pageSize: number = 10;
  private dataReady: boolean = false;
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


  constructor(private route: ActivatedRoute, private showService: ShowService) { }

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    console.log(this.resultsFor);
    switch (this.resultsFor) {
      case 'EVENT':
        this.loadShowsFilteredByEventName(this.route.snapshot.queryParamMap.get('name'));
        break;
      case 'LOCATION':
        this.loadShowsFilteredByLocationID(this.route.snapshot.queryParamMap.get('id'));
        break;
      case 'ATTRIBUTES':
        this.loadShowsFilteredByShowAttributes(
          this.route.snapshot.queryParamMap.get('eventName'),
          this.route.snapshot.queryParamMap.get('hallName'),
          this.route.snapshot.queryParamMap.get('dateFrom'),
          this.route.snapshot.queryParamMap.get('dateTo'),
          this.route.snapshot.queryParamMap.get('timeFrom'),
          this.route.snapshot.queryParamMap.get('timeTo'),
          this.minPrice = +this.route.snapshot.queryParamMap.get('minPrice'),
          this.maxPrice = +this.route.snapshot.queryParamMap.get('maxPrice'),
          this.duration = +this.route.snapshot.queryParamMap.get('duration')
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  /**
   * Convert the resultsFor variable to a string with only first letter capital
   */
  /*
  private toWord(resultsFor: ResultsFor): string {
    let asString = ResultsFor[resultsFor];
    asString = asString[0] + asString.slice(1, asString.length).toLocaleLowerCase();
    return asString;
  }
  */

  // OK
  private loadShowsFilteredByEventName(eventName) {
    console.log('ShowResultsComponent: loadShowsFilteredByEventName');
    this.showService.findShowsFilteredByEventName(eventName).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  // OK
  private loadShowsFilteredByLocationID(id) {
    console.log('ShowResultsComponent: loadShowsFilteredByLocationID');
    this.showService.findShowsFilteredByLocationID(id).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private loadShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice: number, maxPrice: number, duration: number) {
    console.log('ShowResultsComponent: loadShowsFilteredByShowAttributes');
    this.showService.findShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice, duration).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
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
