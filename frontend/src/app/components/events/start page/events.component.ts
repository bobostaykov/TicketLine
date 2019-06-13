import { Component, OnInit } from '@angular/core';
import {EventService} from '../../../services/event/event.service';
import {Event} from '../../../dtos/event';
import {User} from '../../../dtos/user';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {EventType} from '../../../datatype/event_type';
import {Artist} from '../../../dtos/artist';

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
  private createEventForm: FormGroup;
  private submitted: boolean = false;
  private selectedEventType: string = null;
  private duration: number = null;
  private eventTypes = ['', 'Theatre', 'Opera', 'Festival', 'Concert', 'Movie', 'Musical', 'Sport'];

  constructor(private eventService: EventService, private formBuilder: FormBuilder) {
    this.createEventForm = this.formBuilder.group({
      eventName: ['', [Validators.required]],
      type: ['', [Validators.required]],
      duration: ['', [Validators.required, Validators.min(1)]],
      artist: ['', [Validators.required]],
      content: ['', []],
      description: ['', []],
    });
  }

  ngOnInit() {
    this.loadEvents();
  }

  private addEvent() {
    this.submitted = true;
    if (this.createEventForm.valid) {
      const event: Event = new Event(null,
        this.createEventForm.controls.eventName.value,
        EventType[this.selectedEventType.toUpperCase()],
        this.createEventForm.controls.description.value,
        this.createEventForm.controls.content.value,
        new Artist(null, this.createEventForm.controls.artist.value),
        this.duration
      );
      this.createEvent(event);
      this.clearCreateEventForm();
    } else {
      console.log('Invalid input');
    }
  }

  private createEvent(event: Event) {
    this.eventService.createEvent(event).subscribe(
      () => {},
      error => { this.defaultServiceErrorHandling(error); },
      () => { this.loadEvents(); }
    );
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

  private clearCreateEventForm() {
    this.createEventForm.reset();
    this.submitted = false;
  }

}
