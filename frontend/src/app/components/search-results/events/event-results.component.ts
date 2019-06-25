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
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  private artistID: number;
  private artistName: string;
  private eventName: string;
  private eventType: string;
  private content: string;
  private description: string;

  private noResultsFound: boolean = false;
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
          this.artistName = this.route.snapshot.queryParamMap.get('artistName'),
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
          this.artistName,
          this.content,
          this.description,
          this.page
        );
        break;
      default:
        this.defaultServiceErrorHandling('No results for this type');
    }
  }

  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadEvents();
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event to handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadEvents();
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
      this.loadEvents();
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

  private loadEventsFilteredByArtist(id, page: number) {
    console.log('Component Event-Results: loadEventsFilteredByArtist');
    this.eventResultsService.findEventsFilteredByArtistID(id, page).subscribe(
      result => {
        this.events = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
  }

  private loadEventsFilteredByAttributes(eventName, eventType, artistName, content, description, page: number) {
    console.log('Component Event-Results: loadEventsFilteredByAttributes');
    this.eventResultsService.findEventsFilteredByAttributes(eventName, eventType, artistName, content, description, page).subscribe(
      result => {
        this.events = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );

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
