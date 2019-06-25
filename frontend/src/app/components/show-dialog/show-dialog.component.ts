import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Show} from '../../dtos/show';
import {PricePattern} from '../../dtos/pricePattern';
import {HallService} from '../../services/hall/hall.service';
import {Hall} from '../../dtos/hall';
import {Event} from '../../dtos/event';
import {EventResultsService} from '../../services/search-results/events/event-results.service';
import {HallRequestParameter} from '../../datatype/requestParameters/HallRequestParameter';

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
  private patternName: string = 'NewPricePattern';
  private pricePattern: PricePattern = {
    id: undefined,
    name: this.patternName,
    priceMapping: { Cheap: this.cheap, Average: this.average, Expensive: this.expensive }
  };
  private showModel: Show = new Show(null, null, null, null, null, null, 0, this.pricePattern);

  // drop down menu for choosing an event
  private chosenEvent: Event;
  private events: Event[];
  private eventConfig = {
    displayKey: 'name',
    search: true,
    height: 'auto',
    placeholder: ' ',
    limitTo: 10,
    moreText: 'more',
    noResultsFound: 'No results found!',
    searchPlaceholder: 'Search',
    searchOnKey: 'name'
  };

  // drop down menu for choosing a hall
  private chosenHall: Hall;
  private halls: Hall[];
  private hallConfig = {
    displayKey: 'name',
    search: true,
    height: 'auto',
    placeholder: ' ',
    limitTo: 10,
    moreText: 'more',
    noResultsFound: 'No results found!',
    searchPlaceholder: 'Search',
    searchOnKey: 'name'
  };

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
      result => { this.events = result['content']; },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }

  /**
   * Loads Halls for the drop down menu
   */
  private loadHalls() {
    console.log('ShowDialog: loadHalls');
    this.hallService.searchHalls(null, null, [HallRequestParameter.ID, HallRequestParameter.NAME]).subscribe(
      halls => { this.halls = halls; },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }

  /**
   * on submission of form emit event with show to listening parent
   */
  private onSubmit() {
    console.log('ShowDialog: onSubmit');
    this.showModel.pricePattern = {
      id: this.showModel.pricePattern.id,
      name: this.patternName,
      priceMapping: { Cheap: this.cheap, Average: this.average, Expensive: this.expensive }
    };
    this.showModel.event = this.chosenEvent;
    this.showModel.hall = this.chosenHall;
    console.log('ShowDialog showModel: ' + this.showModel); // the updated showModel before sending the request
    this.submitShow.emit(this.showModel);
  }

  /**
   * runs on change of input variable show
   * updates showModel linked to form with show input
   * @param changes list of changes to component
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['show']) {
      if (this.show !== undefined) {
        this.showModel = Object.assign({}, this.show);
        this.chosenEvent = this.showModel.event;
        this.chosenHall = this.showModel.hall;
        this.pricePattern = this.showModel.pricePattern;
        this.patternName = this.pricePattern.name;
        this.cheap = this.pricePattern.priceMapping['Cheap'];
        this.average = this.pricePattern.priceMapping['Average'];
        this.expensive = this.pricePattern.priceMapping['Expensive'];
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
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.error;
    }
  }
}
