import { Component, OnInit } from '@angular/core';
import {EventService} from '../../../services/event/event.service';
import {Event} from '../../../dtos/event';
import {Router} from '@angular/router';

@Component({
  selector: 'app-top-ten-events',
  templateUrl: './top-ten-events.component.html',
  styleUrls: ['./top-ten-events.component.scss']
})
export class TopTenEventsComponent implements OnInit {

  error: boolean = false;
  errorMessage: string = '';
  categories = ['all', 'concert', 'cabaret/comedy', 'sport', 'musical', 'theatre'];
  months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];
  selectedMonth = 'Month';
  // for chart
  colorScheme = {domain: ['#fa8585']};
  events: Event[];
  data = [
    {'name': 'Event3', 'value': 17000},
    {'name': 'Event1', 'value': 12000},
    {'name': 'Event2', 'value': 8000}
  ];

  constructor(private eventService: EventService, private router: Router) { }

  ngOnInit() {
    this.loadEvents();
  }

  setSelectedMonth(month: string) {
    this.selectedMonth = month;
  }

  /**
   * Load top ten events from backend
   */
  loadEvents() {
    this.eventService.getTopTenEvents().subscribe(
      (events: Event[]) => { this.events = events.slice(0, 10); },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }

  goToTickets() {
    this.router.navigate(['/tickets']);
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
