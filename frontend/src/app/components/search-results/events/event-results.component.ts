import { Component, OnInit } from '@angular/core';
import {EventResultsService} from '../../../services/search-results/events/event-results.service';
import {ActivatedRoute} from '@angular/router';
import {Event} from '../../../dtos/event';

@Component({
  selector: 'app-event-results',
  templateUrl: './event-results.component.html',
  styleUrls: ['./event-results.component.scss']
})
export class EventResultsComponent implements OnInit {

  private page: number = 0;
  private pages: Array<number>;
  private dataReady: boolean = false;

  private artistID: number;
  private eventName: string;
  private eventType: string;
  private content: string;
  private description: string;

  private error: boolean = false;
  private errorMessage: string = '';

  private resultsFor: string;

  private events: Event[];
  private headers: string[] = [
    'Name',
    'Category',
    'Artist',
    'Content',
    'Description'
  ];

  constructor(private route: ActivatedRoute, private eventResultsService: EventResultsService) {}

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    console.log(this.resultsFor);
    switch (this.resultsFor) {
      case 'ARTIST':
        this.artistID = +this.loadEventsFilteredByArtist( this.route.snapshot.queryParamMap.get('id'), this.page);
        break;
      case 'ATTRIBUTES':
        this.loadEventsFilteredByAttributes(
          this.eventName = this.route.snapshot.queryParamMap.get('eventName'),
          this.eventType = this.route.snapshot.queryParamMap.get('eventType'),
          this.content = this.route.snapshot.queryParamMap.get('content'),
          this.description = this.route.snapshot.queryParamMap.get('description'),
          this.page
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private loadEvents() {
    switch (this.resultsFor) {
      case 'ARTIST':
        this.loadEventsFilteredByArtist(this.artistID, this.page);
        break;
      case 'ATTRIBUTES':
        this.loadEventsFilteredByAttributes(
          this.eventName,
          this.eventType,
          this.content,
          this.description,
          this.page
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadEvents();
  }

  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadEvents();
    }
  }

  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.pages.length - 1) {
      this.page++;
      this.loadEvents();
    }
  }

  private loadEventsFilteredByArtist(id, page: number) {
    console.log('Component Event-Results: loadEventsFilteredByArtist');
    this.eventResultsService.findEventsFilteredByArtistID(id, page).subscribe(
      result => {
        this.events = result['content'];
        this.pages = new Array(result['totalPages']);
      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private loadEventsFilteredByAttributes(eventName, eventType, content, description, page: number) {
    console.log('Component Event-Results: loadEventsFilteredByAttributes');
    this.eventResultsService.findEventsFilteredByAttributes(eventName, eventType, content, description, page).subscribe(
      result => {
        this.events = result['content'];
        this.pages = new Array(result['totalPages']);
      },
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
