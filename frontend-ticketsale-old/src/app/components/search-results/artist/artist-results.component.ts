import { Component, OnInit } from '@angular/core';
import {Artist} from '../../../dtos/artist';
import {ArtistResultsService} from '../../../services/artist/results/artist-results.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-artist-results',
  templateUrl: './artist-results.component.html',
  styleUrls: ['./artist-results.component.scss']
})
export class ArtistResultsComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';
  private artists: Artist[] = [];
  private artistName: string;
  private loaded: boolean = false;

  constructor(private artistResultsService: ArtistResultsService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.artistName = this.route.snapshot.queryParamMap.get('artist_name');
    this.loadArtists();
  }


  private loadArtists() {
    this.artistResultsService.findArtists(this.artistName).subscribe(
      (artists: Artist[]) => { this.artists = artists; },
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loaded = true; }
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
