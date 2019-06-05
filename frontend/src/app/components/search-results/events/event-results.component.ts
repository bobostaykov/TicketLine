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

  private page: number = 1;
  private pageSize: number = 10;
  private dataReady: boolean = false;
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
        this.loadEventsFilteredByArtist( this.route.snapshot.queryParamMap.get('id'));
        break;
      case 'ATTRIBUTES':
        this.loadEventsFilteredByAttributes(
          this.route.snapshot.queryParamMap.get('eventName'),
          this.route.snapshot.queryParamMap.get('eventType'),
          this.route.snapshot.queryParamMap.get('content'),
          this.route.snapshot.queryParamMap.get('description')
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private loadEventsFilteredByArtist(id) {
    console.log('Component Event-Results: loadEventsFilteredByArtist');
    this.eventResultsService.findEventsFilteredByArtistID(id).subscribe(
      (events: Event[]) => {this.events = events; },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private loadEventsFilteredByAttributes(eventName, eventType, content, description) {
    console.log('Component Event-Results: loadEventsFilteredByAttributes');
    this.eventResultsService.findEventsFilteredByAttributes(eventName, eventType, content, description).subscribe(
      (events: Event[]) => {this.events = events; },
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
