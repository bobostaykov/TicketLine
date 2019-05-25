import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {LocationsService} from '../../../services/search-results/locations/locations.service';
import {Location} from '../../../dtos/location';

// TODO remove all console.log()

@Component({
  selector: 'app-locations',
  templateUrl: './locations.component.html',
  styleUrls: ['./locations.component.scss']
})
export class LocationsComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';
  private resultsFor: string;
  private locations: Location[];
  private headers: string[] = [
    'Country',
    'City',
    'Postcode',
    'Street',
    'Description'
  ];

  constructor(private route: ActivatedRoute, private locationsService: LocationsService) { }

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    console.log(this.resultsFor);
    if (this.resultsFor === 'ATTRIBUTES') {
      this.loadLocationsFiltered(
        this.route.snapshot.queryParamMap.get('country'),
        this.route.snapshot.queryParamMap.get('city'),
        this.route.snapshot.queryParamMap.get('street'),
        this.route.snapshot.queryParamMap.get('postalCode'),
        this.route.snapshot.queryParamMap.get('description')
      );
    } else {
      this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private loadLocationsFiltered(country, city, street, postalCode, description) {
    console.log('Location Component: loadLocationsFiltered');
    this.locationsService.findLocationsFiltered(country, city, street, postalCode, description).subscribe(
      (locations: Location[]) => { this.locations = locations; },
      error => {this.defaultServiceErrorHandling(error); }
    );
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
