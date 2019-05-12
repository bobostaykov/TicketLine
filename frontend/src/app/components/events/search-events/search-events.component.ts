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
  private submitted: boolean = false;
  private artistForm: FormGroup;

  constructor(private router: Router) { }

  ngOnInit() {
    this.artistForm = new FormGroup({
      artist_name: new FormControl('', [Validators.required, Validators.maxLength(60)])
    });
  }

  private submitArtistForm() {
    this.submitted = true;
    if (this.artistName !== '') {
      this.router.navigate(['/events/search/results/artist'], { queryParams: { artist_name: this.artistName } });
    }
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.artistForm.controls[controlName].hasError(errorName);
  }

}
