import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PriceCategory} from '../../../dtos/priceCategory';
import {Hall} from '../../../dtos/hall';
import {Location} from '../../../dtos/location';
import {HallService} from '../../../services/hall/hall.service';
import {ShowResultsService} from '../../../services/search-results/shows/show-results.service';
import {Observable, Subject} from 'rxjs';
import {Show} from '../../../dtos/show';
import {HallRequestParameter} from '../../../datatype/requestParameters/HallRequestParameter';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {LocationResultsService} from '../../../services/search-results/locations/location-results.service';
import {TicketSessionService} from '../../../services/ticket-session/ticket-session.service';
import {ShowRequestParameter} from '../../../datatype/requestParameters/ShowRequestParameter';
import {AuthService} from '../../../services/auth/auth.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ValidateTypeofObject} from '../../../validators/typeobject.validator';
import {ValidateSeatOrSector} from '../../../validators/seatorsector.validator';

@Component({
  selector: 'app-floorplan-control',
  templateUrl: './floorplan-control.component.html',
  styleUrls: ['./floorplan-control.component.scss']
})
export class FloorplanControlComponent implements OnInit {

  // initialization of new hall entity which users can edit and persist to backend
  private newHall: Hall = new Hall(null, 'New Hall', null, [], [], true);
  // observables containing search suggestions for halls, locations and shows respectively
  private halls$: Observable<Hall[]>;
  private locations$: Observable<Location[]>;
  private shows$: Observable<Show[]>;
  // rxjs subjects which are used to both query backend and display search suggestions for hall, location and show search
  private searchHalls = new Subject<string>();
  private searchLocations = new Subject<string>();
  private searchShows = new Subject<string>();
  // price categories to loop through. Still needs to be updated with show specific categories
  // noinspection JSMismatchedCollectionQueryUpdate
  private priceCategories: string[] = Object.keys(PriceCategory);
  // form groups to add seats/sectors and persist new halls to backend
  private addSeatsForm: FormGroup;
  private addSectorsForm: FormGroup;
  private createHallForm: FormGroup;
  // variable for selection mechanism to choose if new hall should contain seats or sectors
  private hallType: string = 'seats';
  // determines if editing the currently selected hall is still allowed
  private editingEnabled: boolean = true;
  // reference to modal to confirm adding/updating hall and adding name to it
  @ViewChild('enterHallName') modalReference: ElementRef;
  // boolean to check if create hall form has been submitted and only then display errors
  private hallFormSubmitted: boolean = false;

  constructor(private hallService: HallService,
              private locationResultsService: LocationResultsService,
              private showResultsService: ShowResultsService,
              private ticketSession: TicketSessionService,
              private authService: AuthService,
              private modalService: NgbModal,
              private route: ActivatedRoute) {
  }

  /**
   * run on initialization of component
   * initializes necessary form groups
   * gets available data from ticketSession Service
   */
  ngOnInit(): void {
    this.buildSeatForm();
    this.buildSectorForm();
    this.buildHallForm();
    // get show first from route parameters and if none is passed there from ticket session service
    // else do not get show
    const id: number = +this.route.snapshot.queryParamMap.get('show_id');
    if (id && id > 0) {
      this.loadSelectedShow(id);
      this.editingEnabled = false;
    } else if (this.ticketSession.getShow() && this.ticketSession.getShow().hall) {
      const hall = this.ticketSession.getShow().hall;
      this.createHallForm.patchValue({
        'hallSelection': hall,
        'showSelection': this.ticketSession.getShow()
      });
      if (hall.seats && hall.seats.length > 0) {
        this.hallType = 'seats';
      } else if (hall.sectors && hall.sectors.length > 0) {
        this.hallType = 'sectors';
      }
      this.editingEnabled = false;
    }
  }

  /**
   * checks if currently logged in user is admin
   */
  private isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * submit method for addSeatsForm
   * adds seats according to parameters entered by user
   * seats are added from seatRowStart to seatRowEnd for numbers seatNumberStart to seatNumberEnd
   * afterwards resets form
   */
  private addSeats(): void {
    // only submit form if user is admin and editing is enabled for currently selected hall. Form must also be valid
    if (this.addSeatsForm.valid && this.isAdmin() && this.editingEnabled) {
      const value = this.addSeatsForm.value;
      // if one of the seat row or number values is null, set equal to other seat row/number value
      value.seatRowStart = value.seatRowStart === null ? value.seatRowEnd : value.seatRowStart;
      value.seatRowEnd = value.seatRowEnd === null ? value.seatRowStart : value.seatRowEnd;
      value.seatNumberStart = value.seatNumberStart === null ? value.seatNumberEnd : value.seatNumberStart;
      value.seatNumberEnd = value.seatNumberEnd === null ? value.seatNumberStart : value.seatNumberEnd;
      for (let row = Math.min(value.seatRowStart, value.seatRowEnd); row <= Math.max(value.seatRowStart, value.seatRowEnd); row++) {
        // tslint:disable-next-line:max-line-length
        for (let number = Math.min(value.seatNumberStart, value.seatNumberEnd); number <= Math.max(value.seatNumberStart, value.seatNumberEnd); number++) {
          // only add seat if seat with same number and row does not already exist
          if (!this.getSelectedHall().seats.some(seat => seat.seatRow === row && seat.seatNumber === number)) {
            this.getSelectedHall().seats.push(new Seat(null, number, row, value.seatPrice, null, null));
          }
        }
      }
    }
  }

  /**
   * submit method for addSectorsForm
   * adds sectors according to parameters entered by user
   * sectors are added with numbers starting from sectorNumberStart to sectorNumberEnd
   * afterwards resets form
   */
  private addSectors(): void {
    // only submit form if user is admin and editing is enabled for currently selected hall. Form must also be valid
    if (this.addSectorsForm.valid && this.isAdmin() && this.editingEnabled) {
      const value = this.addSectorsForm.value;
      // if one of the sector number values is null, set equal to other sector number value
      value.sectorNumberStart = value.sectorNumberStart === null ? value.sectorNumberEnd : value.sectorNumberStart;
      value.sectorNumberEnd = value.sectorNumberEnd === null ? value.sectorNumberStart : value.sectorNumberEnd;
      // tslint:disable-next-line:max-line-length
      for (let number = Math.min(value.sectorNumberStart, value.sectorNumberEnd); number <= Math.max(value.sectorNumberStart, value.sectorNumberEnd); number++) {
        // only add sector if a sector with the same number does not already exist
        if (!this.getSelectedHall().sectors.some(sector => sector.sectorNumber === number)) {
          this.getSelectedHall().sectors.push(new Sector(null, number, value.sectorPrice, null, value.maxCapacity, null, null));
        }
      }
      this.addSectorsForm.reset({
        'sectorPrice': 'Cheap'
      });
    }
  }

  /**
   * another method to submit addSeatsForm
   * deletes seats from seatNumberStart to seatNumberEnd in rows seatRowStart to seatRowEnd
   * afterwards resets form
   */
  private deleteSeats(): void {
    // only submit form if user is admin and editing is enabled for currently selected hall. Form must also be valid
    if (this.addSeatsForm.valid && this.isAdmin() && this.editingEnabled) {
      const value = this.addSeatsForm.value;
      // if one of the seat row or number values is null, set equal to other seat row/number value
      value.seatRowStart = value.seatRowStart === null ? value.seatRowEnd : value.seatRowStart;
      value.seatRowEnd = value.seatRowEnd === null ? value.seatRowStart : value.seatRowEnd;
      value.seatNumberStart = value.seatNumberStart === null ? value.seatNumberEnd : value.seatNumberStart;
      value.seatNumberEnd = value.seatNumberEnd === null ? value.seatNumberStart : value.seatNumberEnd;
      for (let row = Math.min(value.seatRowStart, value.seatRowEnd); row <= Math.max(value.seatRowStart, value.seatRowEnd); row++) {
        // tslint:disable-next-line:max-line-length
        for (let number = Math.min(value.seatNumberStart, value.seatNumberEnd); number <= Math.max(value.seatNumberStart, value.seatNumberEnd); number++) {
          const hall = this.getSelectedHall();
          for (let i = 0; i < hall.seats.length; i++) {
            if (hall.seats[i].seatNumber === number && hall.seats[i].seatRow === row) {
              hall.seats.splice(i, 1);
            }
          }
        }
      }
      this.addSeatsForm.reset({
        'seatPrice': PriceCategory.Cheap
      });
    }
  }

  /**
   * another method to submit addSectorsForm
   * deletes sectors from sectorNumberStart to sectorNumberEnd
   * afterwards resets form
   */
  private deleteSectors(): void {
    // only submit form if user is admin and editing is enabled for currently selected hall. Form must also be valid
    if (this.addSectorsForm.valid && this.isAdmin() && this.editingEnabled) {
      const value = this.addSectorsForm.value;
      // if one of the sector number values is null, set equal to other seat row/number value
      value.sectorNumberStart = value.sectorNumberStart === null ? value.sectorNumberEnd : value.sectorNumberStart;
      value.sectorNumberEnd = value.sectorNumberEnd === null ? value.sectorNumberStart : value.sectorNumberEnd;
      // tslint:disable-next-line:max-line-length
      for (let number = Math.min(value.sectorNumberStart, value.sectorNumberEnd); number <= Math.max(value.sectorNumberStart, value.sectorNumberEnd); number++) {
        const hall = this.getSelectedHall();
        for (let i = 0; i < hall.sectors.length; i++) {
          if (hall.sectors[i].sectorNumber === number) {
            hall.sectors.splice(i, 1);
          }
        }
      }
      this.addSectorsForm.reset({
        'sectorPrice': PriceCategory.Cheap
      });
    }
  }

  /**
   * when user chooses to add or update a hall a modal should be opened asking them to add or confirm name
   * this is handled by this function
   */
  private enterHallNameModal(): void {
    this.hallFormSubmitted = true;
    const hallValid: boolean = this.createHallForm.get('hallSelection').valid;
    const locationValid: boolean = this.createHallForm.get('locationSelection').valid;
    if (hallValid && locationValid && this.isAdmin() && this.editingEnabled) {
      this.modalService.open(this.modalReference, {centered: true});
    }
  }

  /**
   * submit method for createHallForm
   * creates new hall with seats or sectors added by user
   * or updates an already existing hall if editing it is allowed
   * persists hall to backend via hallService
   * afterwards resets form
   */
  private submitHall(): void {
    if (this.createHallForm.valid && this.isAdmin() && this.editingEnabled) {
      const values = this.createHallForm.value;
      const hall = values.hallSelection;
      hall.name = values.hallName;
      hall.location = values.locationSelection;
      if (this.getSelectedHall() === this.newHall) {
        this.hallService.createHall(hall).subscribe(createdHall => {
            this.createHallForm.reset({
              'hallSelection': createdHall,
              'locationSelection': createdHall.location,
              'showSelection': null
            });
            this.editingEnabled = true;
            this.newHall = new Hall(null, 'New Hall', null, [], [], true);
          }
        );
      } else {
        this.hallService.updateHall(hall).subscribe(updatedHall => {
            this.createHallForm.reset({
              'hallSelection': updatedHall,
              'locationSelection': updatedHall.location,
              'showSelection': null
            });
            this.editingEnabled = true;
            this.newHall = new Hall(null, 'New Hall', null, [], [], true);
          }
        );
      }
      this.hallFormSubmitted = false;
    }
  }

  /**
   * deletes a hall by its id
   */
  private deleteHall(): void {
    const hallValid: boolean = this.createHallForm.get('hallSelection').valid;
    if (hallValid && this.getSelectedHall() !== this.newHall && this.isAdmin() && this.editingEnabled) {
      this.hallService.deleteHall(this.getSelectedHall().id).subscribe();
      this.createHallForm.reset({
        'hallSelection': this.newHall,
        'showSelection': null
      });
      this.editingEnabled = true;
    }
  }

  /**
   * called after initialization of component
   * initializes addSeatsForm
   * also notes value changes in form inputs and sets validation accordingly
   * for seatRow and seatNumber only one of start/end has to be set
   */
  private buildSeatForm(): void {
    this.addSeatsForm = new FormGroup({
      'seatRowStart': new FormControl(null, [Validators.required, Validators.min(1)]),
      'seatRowEnd': new FormControl(null, [Validators.required, Validators.min(1)]),
      'seatNumberStart': new FormControl(null, [Validators.required, Validators.min(1)]),
      'seatNumberEnd': new FormControl(null, [Validators.required, Validators.min(1)]),
      'seatPrice': new FormControl(PriceCategory.Cheap, [Validators.required])
    });
    const seatRowStart = this.addSeatsForm.get('seatRowStart');
    const seatRowEnd = this.addSeatsForm.get('seatRowEnd');
    const seatNumberStart = this.addSeatsForm.get('seatNumberStart');
    const seatNumberEnd = this.addSeatsForm.get('seatNumberEnd');

    seatRowStart.valueChanges.subscribe(seat => {
      if (seat === null) {
        seatRowEnd.setValidators([Validators.required, Validators.min(1)]);
      } else {
        seatRowEnd.setValidators(Validators.min(1));
      }
      seatRowEnd.updateValueAndValidity({emitEvent: false});
    });
    seatRowEnd.valueChanges.subscribe(seat => {
      if (seat === null) {
        seatRowStart.setValidators([Validators.required, Validators.min(1)]);
      } else {
        seatRowStart.setValidators(Validators.min(1));
      }
      seatRowStart.updateValueAndValidity({emitEvent: false});
    });
    seatNumberStart.valueChanges.subscribe(seat => {
      if (seat === null) {
        seatNumberEnd.setValidators([Validators.required, Validators.min(1)]);
      } else {
        seatNumberEnd.setValidators(Validators.min(1));
      }
      seatNumberEnd.updateValueAndValidity({emitEvent: false});
    });
    seatNumberEnd.valueChanges.subscribe(seat => {
      if (seat === null) {
        seatNumberStart.setValidators([Validators.required, Validators.min(1)]);
      } else {
        seatNumberStart.setValidators(Validators.min(1));
      }
      seatNumberStart.updateValueAndValidity({emitEvent: false});
    });
  }

  /**
   * called after initialization of component
   * initializes addSectorsForm and also notes value changes to set validators accordingly
   * only one of sectorNumberStart and sectorNumberEnd have to be set
   */
  private buildSectorForm(): void {
    this.addSectorsForm = new FormGroup({
      'sectorNumberStart': new FormControl(null, [Validators.required, Validators.min(1)]),
      'sectorNumberEnd': new FormControl(null, [Validators.required, Validators.min(1)]),
      'maxCapacity': new FormControl(null, [Validators.required, Validators.min(1)]),
      'sectorPrice': new FormControl(PriceCategory.Cheap, [Validators.required])
    });

    const sectorNumberStart = this.addSectorsForm.get('sectorNumberStart');
    const sectorNumberEnd = this.addSectorsForm.get('sectorNumberEnd');

    sectorNumberStart.valueChanges.subscribe(seat => {
      if (seat === null) {
        sectorNumberEnd.setValidators([Validators.required, Validators.min(1)]);
      } else {
        sectorNumberEnd.setValidators(Validators.min(1));
      }
      sectorNumberEnd.updateValueAndValidity({emitEvent: false});
    });
    sectorNumberEnd.valueChanges.subscribe(seat => {
      if (seat === null) {
        sectorNumberStart.setValidators([Validators.required, Validators.min(1)]);
      } else {
        sectorNumberStart.setValidators(Validators.min(1));
      }
      sectorNumberStart.updateValueAndValidity({emitEvent: false});
    });
  }

  /**
   * called after initialization of component
   * initialized createHallForm which allows users to persists halls to backend or delete them there
   * also initializes rxjs subjects and changes them when createHallForm is changed
   * this allows HTML to display search suggestions when user looks after a specific hall, show or location
   */
  private buildHallForm() {
    // custom validator was created for hall and location selection because otherwise I couldn't have used mat-error
    this.createHallForm = new FormGroup({
      'hallSelection': new FormControl(this.newHall, [Validators.required, ValidateTypeofObject, ValidateSeatOrSector]),
      'showSelection': new FormControl(null),
      'locationSelection': new FormControl(null, [Validators.required, ValidateTypeofObject]),
      'hallName': new FormControl(null, [Validators.required])
    });
    const hallSelection = this.createHallForm.get('hallSelection');
    const locationSelection = this.createHallForm.get('locationSelection');
    const showSelection = this.createHallForm.get('showSelection');

    this.halls$ = this.searchHalls.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((hallName: string) => this.hallService.searchHalls(hallName, null, [HallRequestParameter.ID, HallRequestParameter.NAME])
      ));
    hallSelection.valueChanges.subscribe(hall => {
      if (typeof hall !== 'object') {
        this.searchHalls.next(hall);
      }
    });
    this.locations$ = this.searchLocations.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((locationName: string) => this.locationResultsService.getSearchSuggestions(locationName))
    );
    locationSelection.valueChanges.subscribe((location: string) => {
      if (typeof location !== 'object') {
        this.searchLocations.next(location);
      }
    });
    this.shows$ = this.searchShows.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((showTerm: string) => {
        console.log(showTerm);
        const showComponents: string[] = showTerm.split(',');
        console.log(showComponents);
        const eventName: string = showComponents[0];
        const date: string = showComponents[1];
        const time: string = showComponents[2];
        return this.showResultsService.getSearchSuggestions(eventName, date, time);
      })
    );
    showSelection.valueChanges.subscribe(showTerm => {
      if (typeof showTerm !== 'object') {
        this.searchShows.next(showTerm);
      }
    });
  }


  /**
   * interacts with backend via showResultsService and finds single show by its id
   * @param selectedShow for which to return entire entity from backend
   */
  private loadSelectedShow(selectedShow: Show | number): void {
    const id = typeof selectedShow === 'number' ? selectedShow : selectedShow.id;
    this.showResultsService.findOneById(id, [ShowRequestParameter.TICKETS]).subscribe(
      show => {
        this.createHallForm.patchValue({
          'showSelection': show,
          'hallSelection': show.hall,
          'locationSelection': show.hall.location,
        });
        if (show.hall.seats && show.hall.seats.length > 0) {
          this.hallType = 'seats';
        } else if (show.hall.sectors && show.hall.sectors.length > 0) {
          this.hallType = 'sectors';
        }
        this.ticketSession.changeShow(show);
        this.editingEnabled = false;
      }
    );
  }

  /**
   * interacts with backend via locationResultsService and finds single location by its id
   * @param selectedLocation for which to return entire entity from backend
   */
  private loadSelectedLocation(selectedLocation: Location): void {
    this.locationResultsService.findOneById(selectedLocation.id).subscribe(
      location => {
        let hall = this.getSelectedHall();
        let show = this.getSelectedShow();
        let hallName = this.createHallForm.get('hallName').value;
        if (hall !== this.newHall && hall.location) {
          hall = hall.location.id === location.id ? hall : null;
          show = hall ? show : null;
          hallName = hall ? hall.name : null;
          this.ticketSession.changeShow(show);
        }
        this.createHallForm.patchValue({
          'locationSelection': location,
          'hallSelection': hall,
          'showSelection': show,
          'hallName': hallName
        });
        if (hall.seats && hall.seats.length > 0) {
          this.hallType = 'seats';
        } else if (hall.sectors && hall.sectors.length > 0) {
          this.hallType = 'sectors';
        }
      }
    );
  }

  /**
   * interacts with backend via hallService and finds single hall by its id
   * @param selectedHall for which to return entire entity from backend
   */
  private loadSelectedHall(selectedHall: Hall): void {
    if (selectedHall !== this.newHall) {
      this.hallService.findOneById(selectedHall.id, [HallRequestParameter.EDITING]).subscribe(
        hall => {
          const show = this.getSelectedShow() && this.getSelectedShow().hall.id === hall.id ? this.getSelectedShow() : null;
          this.ticketSession.changeShow(show);
          this.createHallForm.patchValue({
            'hallSelection': hall,
            'locationSelection': hall.location,
            'showSelection': show,
            'hallName': hall.name
          });
          if (hall.seats && hall.seats.length > 0) {
            this.hallType = 'seats';
          } else if (hall.sectors && hall.sectors.length > 0) {
            this.hallType = 'sectors';
          }
          this.editingEnabled = hall.editingEnabled;
        }
      );
    } else {
      this.createHallForm.patchValue({
        'showSelection': null,
        'hallName': null
      });
      this.editingEnabled = true;
    }
  }

  /**
   * display function for show.
   * @param show string which is displayed as option in show selection menu
   * consists of the name of the show's associated event, the show date and the show time attributes
   */
  private displayShow(show?: Show): string | undefined {
    return show ? show.event.name + ', ' + show.date + ', ' + show.time : undefined;
  }

  /**
   * display function for location
   * @param location for which to return locationName to display
   */
  private displayLocation(location?: Location): string | undefined {
    return location ? location.locationName : undefined;
  }

  /**
   * display function for hall
   * @param hall for which to return name to display
   */
  private displayHall(hall?: Hall): string | undefined {
    return hall ? hall.name : undefined;
  }

  /**
   * getter
   * returns hall entity currently selected in createHallForm
   */
  private getSelectedHall(): Hall {
    return this.createHallForm.get('hallSelection').value;
  }

  /**
   * getter
   * returns show entity currently selected in createHallForm
   */
  private getSelectedShow(): Show {
    return this.createHallForm.get('showSelection').value;
  }
}


