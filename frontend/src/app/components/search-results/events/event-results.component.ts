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

  private error: boolean = false;
  private errorMessage: string = '';
  private resultsFor: string;
  private events: Event[];
  private headers: string[] = [
    'Name',
    'Category',
    'Artist',
    'Description',
    'Content',
    'Buy'
  ];

  constructor(private route: ActivatedRoute, private eventResultsService: EventResultsService) {}

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    switch (this.resultsFor) {
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

  private loadEventsFilteredByAttributes(eventName, eventType, content, description) {
    console.log('Component Event-Results: loadEventsFilteredByAttributes');
    this.eventResultsService.findEventsFilteredByAttributes(eventName, eventType, content, description).subscribe(
      (events: Event[]) => {this.events = events; },
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
