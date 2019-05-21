import { Component, OnInit } from '@angular/core';
import {EventService} from '../../../services/event/event.service';
import {Event} from '../../../dtos/event';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';
  private dataReady: boolean = false;
  private events: Event[];
  private page: number = 1;
  private pageSize: number = 10;
  private headers: string[] = [
    'Name',
    'Type',
    'Artist'
  ];

  constructor(private eventService: EventService) { }

  ngOnInit() {
    this.loadEvents();
  }

  private loadEvents() {
    this.eventService.getAllEvents().subscribe(
      (events: Event[]) => { this.events = events; },
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true;
        console.log(this.events); }
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