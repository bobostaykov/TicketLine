import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {LocationResultsService} from '../../../services/search-results/locations/location-results.service';
import {Location} from '../../../dtos/location';

@Component({
  selector: 'app-locations',
  templateUrl: './location-results.component.html',
  styleUrls: ['./location-results.component.scss']
})
export class LocationResultsComponent implements OnInit {

  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  private name: string;
  private country: string;
  private city: string;
  private street: string;
  private postalCode: string;
  private description: string;

  private noResultsFound: boolean = false;
  private error: boolean = false;
  private errorMessage: string = '';

  private resultsFor: string;

  private locations: Location[];
  private headers: string[] = [
    'Name',
    'Country',
    'City',
    'Postcode',
    'Street',
    'Description'
  ];

  constructor(private route: ActivatedRoute, private locationsService: LocationResultsService) { }

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    if (this.resultsFor === 'ATTRIBUTES') {
      this.loadLocationsFiltered(
        this.name = this.route.snapshot.queryParamMap.get('name'),
        this.country = this.route.snapshot.queryParamMap.get('country'),
        this.city = this.route.snapshot.queryParamMap.get('city'),
        this.street = this.route.snapshot.queryParamMap.get('street'),
        this.postalCode = this.route.snapshot.queryParamMap.get('postalCode'),
        this.description = this.route.snapshot.queryParamMap.get('description'),
        this.page
      );
    } else {
      this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private loadLocationsFiltered(name, country, city, street, postalCode, description, page) {
    console.log('Location Component: loadLocationsFiltered');
    this.locationsService.findLocationsFiltered(name, country, city, street, postalCode, description, page).subscribe(
      result => {
        this.locations = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
    );
  }

  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadLocationsFiltered(this.name, this.country, this.city, this.street, this.postalCode, this.description, this.page);
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event to handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadLocationsFiltered(this.name, this.country, this.city, this.street, this.postalCode, this.description, this.page);
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
      this.loadLocationsFiltered(this.name, this.country, this.city, this.street, this.postalCode, this.description, this.page);
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
