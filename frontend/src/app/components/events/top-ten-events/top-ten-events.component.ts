import {Component, OnInit} from '@angular/core';
import {EventService} from '../../../services/event/event.service';
import {Router} from '@angular/router';
import {EventTickets} from '../../../dtos/event_tickets';
import {TopTenDetails} from '../../../dtos/top-ten-details';

@Component({
  selector: 'app-top-ten-events',
  templateUrl: './top-ten-events.component.html',
  styleUrls: ['./top-ten-events.component.scss']
})
export class TopTenEventsComponent implements OnInit {

  constructor(private eventService: EventService, private router: Router) { }

  width: number;
  height: number;
  error: boolean = false;
  errorMessage: string = '';
  dataReady: boolean = false;
  categories: Map<string, boolean> = new Map([
    ['all', true],
    ['concert', false],
    ['opera', false],
    ['sport', false],
    ['musical', false],
    ['theatre', false],
    ['festival', false],
    ['movie', false],
  ]);
  months: string[] = [
    'all',
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
  selectedMonth: string = 'Month';
  colorScheme: any = {domain: ['#fa8585']};
  data: any = [];


  /**
   * Transform the events array to a 'data' dictionary suitable for the chart
   */
  private eventsToData(events: EventTickets[]) {
    const data = [];
    for (let i = 0; i < events.length; i++) {
      data.push({'name': events[i].eventName, 'value': events[i].ticketsSold});
    }
    return data;
  }


  ngOnInit() {
    this.width = window.innerWidth * 0.63;
    this.height = window.innerHeight * 0.5;
    this.loadEvents(['all']);
  }


  private selectMonth(month: string) {
    this.selectedMonth = month;

    const categoriesArray: string[] = [];
    this.categories.forEach((value: boolean, key: string) => {
      if (value) {
        categoriesArray.push(key);
      }
    });

    this.loadEvents(categoriesArray);
  }


  /**
   * Load top ten events from backend
   */
  private loadEvents(categoriesArray: string[]) {
    let monthsArray: string[] = [];
    if (this.selectedMonth === 'Month' || this.selectedMonth === 'all') {
      monthsArray = this.months;
    } else {
      monthsArray = [this.selectedMonth];
    }

    if (categoriesArray.length === 1 && categoriesArray[0] === 'all') {
      categoriesArray.pop();
      this.categories.forEach((value: boolean, key: string) => {
        if (key !== 'all') {
          categoriesArray.push(key.toUpperCase());
        }
      });
    } else {
      for (let i = 0; i < categoriesArray.length; i++) {
        categoriesArray[i] = categoriesArray[i].toUpperCase();
      }
    }

    const monthsCats = new TopTenDetails(monthsArray, categoriesArray);

    this.eventService.getTopTenEvents(monthsCats).subscribe(
      (events: EventTickets[]) => { this.data = this.eventsToData(events.slice(0, 10)); },
      error => { this.defaultServiceErrorHandling(error); },
    () => { this.dataReady = true; }
    );
  }


  /**
   * On checkbox change reload events
   */
  private reload(category: string) {
    const categoriesArray: string[] = [];
    if (category === 'all') {
      this.categories.forEach((value: boolean, key: string) => {
        if (key !==  'all') {
          this.categories.set(key, false);
        }
      });
    } else {
      this.categories.set('all', false);
    }

    this.categories.forEach((value: boolean, key: string) => {
      if (value) {
        categoriesArray.push(key);
      }
    });

    this.loadEvents(categoriesArray);
  }


  /**
   * Navigate to the shows page
   */
  private viewShows(event: any) {
    this.router.navigate(['/events/search/results/shows'], {queryParams: {
      resultsFor: 'EVENT',
      name:        event.valueOf()['name']
    }});
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
