import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Show} from '../../dtos/show';
import {PricePattern} from '../../dtos/pricePattern';
import {HallService} from '../../services/hall/hall.service';
import {Hall} from '../../dtos/hall';
import {Event} from '../../dtos/event';
import {EventResultsService} from '../../services/search-results/events/event-results.service';

@Component({
  selector: 'app-show-dialog',
  templateUrl: './show-dialog.component.html',
  styleUrls: ['./show-dialog.component.scss']
})
export class ShowDialogComponent implements OnInit, OnChanges {

  @Input() title: string;
  @Input() show: Show;
  @Output() submitShow = new EventEmitter<Show>();

  private cheap: number = 0;
  private average: number = 0;
  private expensive: number = 0;
  private pricePattern: PricePattern = {
    id: undefined,
    name: 'NewPricePattern',
    priceMapping: {'CHEAP': this.cheap, 'AVERAGE': this.average, 'EXPENSIVE': this.expensive}
  };
  private showModel: Show = new Show(null, null, null, null, null, null, 0, this.pricePattern);

  // drop down menu for choosing an event
  private chosenEvent: Event;
  private chosenEventName: string;
  private events: Event[];
  private eventOptions: string[] = [];
  private eventConfig = {
    displayKey: 'Select an event',
    search: true,
    height: '350px',
    placeholder: ' ',
    limitTo: this.eventOptions.length,
    moreText: 'more',
    noResultsFound: 'No results found!',
    searchPlaceholder: 'Search',
    searchOnKey: 'name'
  };

  // drop down menu for choosing a hall
  private chosenHall: Hall;
  private chosenHallName: string;
  private halls: Hall[];
  private hallOptions: string[] = [];
  private hallConfig = {
    displayKey: 'Select a hall',
    search: true,
    height: '350px',
    placeholder: ' ',
    limitTo: this.hallOptions.length,
    moreText: 'more',
    noResultsFound: 'No results found!',
    searchPlaceholder: 'Search',
    searchOnKey: 'name'
  };

  private eventsLoaded: boolean = false;
  private hallsLoaded: boolean = false;

  private error: boolean = false;
  private errorMessage: string = '';

  constructor(private eventResultsService: EventResultsService, private hallService: HallService) {
  }

  ngOnInit() {
    this.loadEvents();
    this.loadHalls();
  }

  /**
   * Loads Events for the drop down menu
   */
  private loadEvents() {
    console.log('ShowDialog: loadEvents');
    this.eventResultsService.findEventsFilteredByAttributes('getAllEventsNotPaginated', null, null, null, null, 0).subscribe(
      result => {
        this.events = result['content'];
        this.eventOptions = this.eventsArrayToStringArray(this.events);
      },
      error => {
        this.defaultServiceErrorHandling(error);
      },
      () => {
        this.eventsLoaded = true;
      }
    );
  }

  /**
   * Loads Halls for the drop down menu
   */
  private loadHalls() {
    console.log('ShowDialog: loadHalls');
    this.hallService.getAllHalls().subscribe(
      halls => {
        this.halls = halls;
        this.hallOptions = this.hallsArrayToStringArray(this.halls);
      },
      error => {
        this.defaultServiceErrorHandling(error);
      },
      () => {
        this.hallsLoaded = true;
      }
    );
  }

  /**
   * Takes the names out of the array and returns them
   * @param events array of events
   */
  private eventsArrayToStringArray(events: Event[]): string[] {
    const toReturn: string[] = [];
    events.forEach(event => {
      toReturn.push(event.name);
    });
    return toReturn;
  }

  /**
   * Takes the names out of the array and returns them
   * @param halls array of halls
   */
  private hallsArrayToStringArray(halls: Hall[]): string[] {
    const toReturn: string[] = [];
    halls.forEach(hall => {
      toReturn.push(hall.name);
    });
    return toReturn;
  }

  /**
   * Returns the found event with the chosenEventName
   */
  private getOneEventByName() {
    console.log('ShowDialog: getOneEventByName');
    this.eventResultsService.findEventsFilteredByAttributes(this.chosenEventName, null, null, null, null, 0).subscribe(
      result => {
        this.chosenEvent = result['content'][0];
        this.showModel.event = this.chosenEvent;
        console.log(result);
        console.log(this.showModel.event.name);
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Returns the found hall with the chosenHallName
   */
  private getOneHallByName() {
    console.log('ShowDialog: getOneHallByName');
    this.hallService.searchHalls(this.chosenHallName, null, null).subscribe(
      halls => {
        this.chosenHall = halls[0];
        this.showModel.hall = this.chosenHall;
        },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * on submission of form emit event with show to listening parent
   */
  private onSubmit() {
    console.log('ShowDialog: onSubmit');
    if (this.chosenHallName !== undefined && this.chosenEventName !== undefined) {
      this.getOneEventByName();
      this.getOneHallByName();
      this.pricePattern.priceMapping = {'CHEAP': this.cheap, 'AVERAGE': this.average, 'EXPENSIVE': this.expensive};
      this.showModel.pricePattern = this.pricePattern;
      this.showModel.event = this.chosenEvent;
      this.showModel.hall = this.chosenHall;
      console.log(this.showModel); // I print the model before returning it
      this.submitShow.emit(this.showModel);
    } else {
      console.log('Not specified event or hall');
    }
  }

  /**
   * runs on change of input variable show
   * updates showModel linked to form with show input
   * @param changes list of changes to component
   */
  ngOnChanges(changes: SimpleChanges): void {
    console.log(this.showModel);
    if (changes['show']) {
      if (this.show !== undefined) {
        console.log(this.show.pricePattern);
        this.showModel = Object.assign({}, this.show);
      }
    }
  }


  /**
   * Error handler
   * @param error message
   */
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
