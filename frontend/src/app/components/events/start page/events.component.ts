import {Component, OnInit} from '@angular/core';
import {EventService} from '../../../services/event/event.service';
import {Event} from '../../../dtos/event';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {

  private page: number = 0;
  private pages: Array<number>;
  private dataReady: boolean = false;
  private error: boolean = false;
  private errorMessage: string = '';
  private events: Event[];

  constructor(private eventService: EventService) {}

  ngOnInit() {
    this.loadEvents();
  }

  private loadEvents() {
    this.eventService.getAllEvents(this.page).subscribe(
      result => {
        this.events = result['content'];
        this.pages = new Array(result['totalPages']);
        console.log(result);
      },
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.dataReady = true; }
    );
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
