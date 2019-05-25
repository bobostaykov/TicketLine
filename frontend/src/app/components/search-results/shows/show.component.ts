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

  // TODO pagination
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
          this.route.snapshot.queryParamMap.get('minPrice'),
          this.route.snapshot.queryParamMap.get('maxPrice'),
          this.route.snapshot.queryParamMap.get('duration')
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
      error => {this.defaultServiceErrorHandling(error); }
    );
  }

  // OK
  private loadShowsFilteredByLocationID(id) {
    console.log('ShowResultsComponent: loadShowsFilteredByLocationID');
    this.showService.findShowsFilteredByLocationID(id).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); }
    );
  }

  private loadShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice, duration) {
    console.log('ShowResultsComponent: loadShowsFilteredByShowAttributes');
    this.showService.findShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice, duration).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); }
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
