import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';

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
  private description: string = '';
  private locationForm: FormGroup;
  // Should the options be listed here
  private countries: string[] = ['Austria', 'Germany', 'France', 'Spain', 'Great Britain', 'Bulgaria', 'Italy', 'Russia', 'USA', 'Mexico'];


  constructor(private router: Router) { }

  ngOnInit() {
    this.artistForm = new FormGroup({
      artist_name: new FormControl('', [Validators.required, Validators.maxLength(60)])
    });

    this.locationForm = new FormGroup({
      country: new FormControl(),
      city: new FormControl(),
      street  : new FormControl(),
      postalCode: new FormControl(),
      description: new FormControl()
      });
  }

  private submitArtistForm() {
    if (this.artistName !== '') {
      this.router.navigate(['/events/search/results/artist'], { queryParams: { artist_name: this.artistName } });
    }
  }

  private searchByLocation(): void {
    console.log(this.locationForm);
    if (this.country !== '' || this.city !== '' || this.street !== '' || this.postalCode !== '' ||
      this.postalCode !== '' || this.description !== '') {
      this.router.navigate(['/shows'], {queryParams: {resultsFor: 'LOCATION', country: this.country, city: this.city, street: this.street, postalCode: this.postalCode, description: this.description}});
    }
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.artistForm.controls[controlName].hasError(errorName);
  }

}
