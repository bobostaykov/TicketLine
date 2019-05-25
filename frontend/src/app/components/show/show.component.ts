import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Show} from '../../dtos/show';
import {ResultsFor} from '../../datatype/results_for';
import {ShowService} from '../../services/show/show.service';

@Component({
  selector: 'app-shows',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.scss']
})
export class ShowComponent implements OnInit {

  private resultsFor: string;
  private name: string;
  private id: string;
  private country: string;
  private city: string;
  private street: string;
  private postalCode: string;
  private description: string;

  private error: boolean = false;
  private errorMessage: string = '';
  private shows: Show[];
  private headElements: string[] = [
    'Event',
    'Artist',
    'Location',
    'Hall',
    'City',
    'Country',
    'Date',
    'Category',
    'Sold Tickets',
    'Buy'
  ];

  constructor(private route: ActivatedRoute, private showService: ShowService) { }

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    console.log(this.resultsFor);
    if (this.resultsFor === 'EVENT') {
      this.loadShowsFilteredByEventName(this.route.snapshot.queryParamMap.get('name'));
    } else if (this.resultsFor === 'LOCATION') {
      this.id = this.route.snapshot.queryParamMap.get('id');
        if (this.id === null) {
          this.country = this.route.snapshot.queryParamMap.get('country');
          this.city = this.route.snapshot.queryParamMap.get('city');
          this.street = this.route.snapshot.queryParamMap.get('street');
          this.postalCode = this.route.snapshot.queryParamMap.get('postalCode');
          this.description = this.route.snapshot.queryParamMap.get('description');
          this.loadShowsFilteredByLocation(this.country, this.city, this.street, this.postalCode, this.description);
        } else {
          this.loadShowsFilteredByLocationID(this.id);
        }
    } else if (this.resultsFor === 'ATTRIBUTES') {
      this.loadShowsFilteredByShowAttributes();
    } else {
      this.defaultServiceErrorHandling('No results for this type');
    }
  }



  /**
   * Convert the resultsFor variable to a string with only first letter capital
   */
  private toWord(resultsFor: ResultsFor): string {
    let asString = ResultsFor[resultsFor];
    asString = asString[0] + asString.slice(1, asString.length).toLocaleLowerCase();
    return asString;
  }


  /**
   * Load shows from backend
   */
  /*
  private loadShows() {
    this.showService.findShows(this.resultsFor, this.name).subscribe(
      (shows: Show[]) => { this.shows = shows; },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }
  */

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

  private loadShowsFilteredByLocation(country, city, street, postalCode, description) {
    console.log('Component: loadShowsFilteredByLocation');
    this.showService.findShowsFilteredByLocation(country, city, street, postalCode, description).subscribe(
      (shows: Show[]) => {this.shows = shows; },
      error => {this.defaultServiceErrorHandling(error); }
    );
  }

  private loadShowsFilteredByShowAttributes() {}

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
