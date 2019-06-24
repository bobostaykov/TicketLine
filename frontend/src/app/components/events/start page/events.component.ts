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
  private totalPages: number;
  private pageRange: Array<number> = [];
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
        this.totalPages = result['totalPages'];
        this.setPagesRange();
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
