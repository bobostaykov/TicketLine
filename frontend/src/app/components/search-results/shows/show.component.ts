import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Show} from '../../../dtos/show';
import {ResultsFor} from '../../../datatype/results_for';
import {ShowService} from '../../../services/search-results/shows/show.service';

@Component({
  selector: 'app-shows',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.scss']
})
export class ShowComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';
  private resultsFor: string;
  private shows: Show[];
  private headElements: string[] = [
    'Event',
    'Artist',
    'Location',
    'Hall',
    'Country - City',
    'Date',
    'Category',
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

  // TODO I know that name is unique, but why dont't we send the id?
  private loadShowsFilteredByEventName(eventName) {
    console.log('Component: loadShowsFilteredByEventName');
    this.showService.findShowsFilteredByEventName(eventName).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); }
    );
  }

  private loadShowsFilteredByLocationID(id) {
    console.log('Component: loadShowsFilteredByLocationID');
    this.showService.findShowsFilteredByLocationID(id).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); }
    );
  }

  private loadShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice, duration) {
    console.log('Component: loadShowsFilteredByShowAttributes');
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
