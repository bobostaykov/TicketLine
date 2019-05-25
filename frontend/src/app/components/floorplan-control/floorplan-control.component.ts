import {Component, OnInit} from '@angular/core';
import {Seat} from '../../dtos/seat';
import {Sector} from '../../dtos/sector';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PriceCategory} from '../../dtos/priceCategory';
import {Hall} from '../../dtos/hall';
import {Location} from '../../dtos/location';
import {HallService} from '../../services/hall/hall.service';
import {LocationService} from '../../services/location/location.service';

@Component({
  selector: 'app-floorplan-control',
  templateUrl: './floorplan-control.component.html',
  styleUrls: ['./floorplan-control.component.scss']
})
export class FloorplanControlComponent implements OnInit {

  // TODO: this is a very large component, split up?
  private newSeatPlan: Hall = new Hall(null, 'New SeatPlan', null, [], null);
  private newSectorPlan: Hall = new Hall(null, 'New SectorPlan', null, null, []);
  private allHalls: Hall[];
  private halls: Hall[];
  private locations: Location[];
  private priceCategories: string[] = Object.keys(PriceCategory);
  private addSeatsForm: FormGroup;
  private addSectorsForm: FormGroup;
  private updateSeatForm: FormGroup;
  private updateSectorForm: FormGroup;
  private createHallForm: FormGroup;
  private selectedSeat: Seat;
  private selectedSector: Sector;
  private sectorAlreadyExists: boolean = false;
  private seatAlreadyExists: boolean = false;


  constructor(private hallService: HallService, private locationService: LocationService) {
  }

  /**
   * run on initialization of component
   * starts loading halls from backend
   * initializes form groups for addSeat and addSector
   */
  ngOnInit(): void {
    // loading halls saved in backend and adding them to the halls  and allHalls array
    this.getAllHalls();
    // initialize locations array with empty location. If selected all halls will be displayed
    // loading locations saved in backend and adding them to the locations array
    this.getAllLocations();
    // initializing forms that allow users to add seats and sectors to new Halls
    this.buildSeatForm();
    this.buildSectorForm();
    this.buildHallForm();
    this.buildUpdateForms();
  }

  /**
   * called on submit of addSeatsForm
   * adds seats according to parameters entered by user
   * afterwards resets form
   */
  // tslint:disable-next-line:max-line-length
  private addSeats(): void {
    if (this.addSeatsForm.valid) {
      const value = this.addSeatsForm.value;
      // if one of the seat row or number values is null, set equal to other seat row/number value
      value.seatRowStart = value.seatRowStart === null ? value.seatRowEnd : value.seatRowStart;
      value.seatRowEnd = value.seatRowEnd === null ? value.seatRowStart : value.seatRowEnd;
      value.seatNumberStart = value.seatNumberStart === null ? value.seatNumberEnd : value.seatNumberStart;
      value.seatNumberEnd = value.seatNumberEnd === null ? value.seatNumberStart : value.seatNumberEnd;
      for (let row = Math.min(value.seatRowStart, value.seatRowEnd); row <= Math.max(value.seatRowStart, value.seatRowEnd); row++) {
        // tslint:disable-next-line:max-line-length
        for (let number = Math.min(value.seatNumberStart, value.seatNumberEnd); number <= Math.max(value.seatNumberStart, value.seatNumberEnd); number++) {
          if (!this.getSelectedHall().seats.some(seat => seat.seatRow === row && seat.seatNumber === number)) {
            this.getSelectedHall().seats.push(new Seat(null, number, row, value.seatPrice));
          }
        }
      }
      this.addSeatsForm.reset({
        'seatPrice': PriceCategory.Cheap
      });
    }
  }

  private deleteSeats(): void {
    if (this.addSeatsForm.valid) {
      const value = this.addSeatsForm.value;
      console.log(value);
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
   * called on submit of addSectorsForm
   * adds sectors according to parameters entered by user
   * afterwards resets form
   */
  private addSectors(): void {
    if (this.addSectorsForm.valid) {
      const value = this.addSectorsForm.value;
      // if one of the sector number values is null, set equal to other seat row/number value
      value.sectorNumberStart = value.sectorNumberStart === null ? value.sectorNumberEnd : value.sectorNumberStart;
      value.sectorNumberEnd = value.sectorNumberEnd === null ? value.sectorNumberStart : value.sectorNumberEnd;
      // tslint:disable-next-line:max-line-length
      for (let number = Math.min(value.sectorNumberStart, value.sectorNumberEnd); number <= Math.max(value.sectorNumberStart, value.sectorNumberEnd); number++) {
        if (!this.getSelectedHall().sectors.some(sector => sector.sectorNumber === number)) {
          this.getSelectedHall().sectors.push(new Sector(null, number, value.sectorPrice));
        }
      }
      this.addSectorsForm.reset({
        'sectorPrice': PriceCategory.Cheap
      });
    }
  }

  private deleteSectors(): void {
    if (this.addSectorsForm.valid) {
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

  private buildHallForm() {
    this.createHallForm = new FormGroup({
      'hallSelection': new FormControl(this.newSeatPlan, [Validators.required]),
      'locationSelection': new FormControl(null, [Validators.required]),
      'floorplanName': new FormControl(null, [Validators.required])
    });
    const hallSelection = this.createHallForm.get('hallSelection');
    const locationSelection = this.createHallForm.get('locationSelection');
    hallSelection.valueChanges.subscribe(hall => {
      if (hall.location !== null) {
        locationSelection.setValue(this.locations.find(location => location.id === hall.location.id), {emitEvent: false});
        console.log(hall);
      }
      this.disableUpdateForms();
    });
    locationSelection.valueChanges.subscribe(location => {
      if (location !== null) {
        this.halls = this.allHalls.filter(hall => hall.location.id === location.id);
      } else {
        this.halls = this.allHalls;
      }
      this.disableUpdateForms();
    });
  }

  private buildUpdateForms() {
    this.updateSeatForm = new FormGroup({
      'seatNumber': new FormControl(null, [Validators.required, Validators.min(1)]),
      'seatRow': new FormControl(null, [Validators.required, Validators.min(1)]),
      'seatPrice': new FormControl(PriceCategory.Cheap, [Validators.required])
    });
    this.updateSectorForm = new FormGroup({
      'sectorNumber': new FormControl(null, [Validators.required, Validators.min(1)]),
      'sectorPrice': new FormControl(PriceCategory.Cheap, [Validators.required])
    });
  }

  /**
   * returns true, if inputs seatRowStart or seatRowError have been edited and are invalid
   * called to determine if error message needs to be displayed
   */
  private seatRowErrors(): boolean {
    const seatRowStart = this.addSeatsForm.get('seatRowStart');
    const seatRowEnd = this.addSeatsForm.get('seatRowEnd');
    return (seatRowStart.dirty || seatRowEnd.dirty) && (seatRowStart.invalid || seatRowEnd.invalid);
  }

  /**
   * returns ture, if inputs seatNumberStart or seatNumberEnd have been edited and are invalid
   * called to determine if error message needs to be displayed
   */
  private seatNumberErrors(): boolean {
    const seatNumberStart = this.addSeatsForm.get('seatNumberStart');
    const seatNumberEnd = this.addSeatsForm.get('seatNumberEnd');
    return (seatNumberStart.dirty || seatNumberEnd.dirty) && (seatNumberStart.invalid || seatNumberEnd.invalid);
  }

  /**
   * called after initialization of component
   * initializes addSectorsForm and determines also notes value changes to set validators accordingly
   * only one of sectorNumberStart and sectorNumberEnd have to be set
   */
  private buildSectorForm(): void {
    this.addSectorsForm = new FormGroup({
      'sectorNumberStart': new FormControl(null, [Validators.required, Validators.min(1)]),
      'sectorNumberEnd': new FormControl(null, [Validators.required, Validators.min(1)]),
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
   * returns true if sectorNumberStart or sectorNumberEnd have been edited and are invalid
   * called to determine if error message needs to be displayed
   */
  private sectorNumberErrors(): boolean {
    const sectorNumberStart = this.addSectorsForm.get('sectorNumberStart');
    const sectorNumberEnd = this.addSectorsForm.get('sectorNumberEnd');
    return (sectorNumberStart.dirty || sectorNumberEnd.dirty) && (sectorNumberStart.invalid || sectorNumberEnd.invalid);
  }

  /**
   * load all halls from backend via hallService
   */
  private getAllHalls(): void {
    console.log('Loading all halls from backend');
    this.hallService.getAllHalls().subscribe(
      halls => {
        this.allHalls = halls;
        this.halls = halls;
      },
      error => console.log(error)
    );
    // this.halls = this.allHalls.map(hall => ({...hall}));
  }

  private getAllLocations(): void {
    console.log('Loading all locations from backend');
    this.locationService.getAllLocations().subscribe(
      locations => this.locations = locations,
      error => console.log(error)
    );
  }

  displayLocation(location: Location) {
    return location !== null ? location.street + ', ' +
      location.city + ' ' + location.postalCode : '';
  }

  private getSelectedHall(): Hall {
    return this.createHallForm.get('hallSelection').value;
  }

  createFloorplan() {
    const values = this.createHallForm.value;
    const hall: Hall = new Hall(
      null,
      values.floorplanName,
      values.locationSelection,
      values.hallSelection.seats,
      values.hallSelection.sectors
    );
    this.hallService.createHall(hall).subscribe();
    this.createHallForm.reset({
      'hallSelection': (values.hallSelection === this.newSeatPlan ? this.newSeatPlan = new Hall(null, 'New SeatPlan', null, [], null) :
        this.newSectorPlan = new Hall(null, 'New SectorPlan', null, null, []))
    });
  }

  private onSvgClick(event: Event) {
    if ((<HTMLElement>event.target).tagName !== 'path') {
      this.disableUpdateForms();
    }
  }

  private disableUpdateForms() {
    document.getElementById('updateSeatForm').style.display = 'none';
    document.getElementById('updateSectorForm').style.display = 'none';
  }

  private updateSeat() {
    const values = this.updateSeatForm.value;
    if (!this.getSelectedHall().seats.some(seat => seat !== this.selectedSeat && seat.seatNumber === this.selectedSeat.seatNumber &&
      seat.seatRow === values.seatRow)) {
      this.selectedSeat.seatNumber = values.seatNumber;
      this.selectedSeat.seatRow = values.seatRow;
      this.selectedSeat.priceCategory = values.seatPrice;
      this.seatAlreadyExists = false;
      this.disableUpdateForms();
    } else {
      this.seatAlreadyExists = true;
    }
  }

  private deleteSeat() {
    const seats: Seat[] = this.createHallForm.get('hallSelection').value.seats;
    seats.splice(seats.indexOf(this.selectedSeat), 1);
    this.disableUpdateForms();
  }

  private deleteSector() {
    const sectors: Sector[] = this.createHallForm.get('hallSelection').value.sectors;
    sectors.splice(sectors.indexOf(this.selectedSector), 1);
    this.disableUpdateForms();
  }

  private updateSector() {
    const values = this.updateSectorForm.value;
    if (!this.getSelectedHall().sectors.some(sector => sector !== this.selectedSector &&
      sector.sectorNumber === values.sectorNumber)) {
      this.selectedSector.sectorNumber = values.sectorNumber;
      this.selectedSector.priceCategory = values.sectorPrice;
      this.sectorAlreadyExists = false;
      this.disableUpdateForms();
    } else {
      this.sectorAlreadyExists = true;
    }
  }

  displaySeatForm(element: { eventTarget: HTMLElement; seat: Seat }) {
    const updateForm = document.getElementById('updateSeatForm');
    updateForm.style.display = 'inline-block';
    const rectEvent = element.eventTarget.getBoundingClientRect();
    const rectUpdateForm = updateForm.getBoundingClientRect();
    updateForm.style.left = rectEvent.left - rectUpdateForm.width / 2 + rectEvent.width / 2 + 'px';
    updateForm.style.top = rectEvent.top + window.scrollY + rectEvent.height + 20 + 'px';
    // update form with seat parameters
    this.selectedSeat = element.seat;
    this.updateSeatForm.setValue({
      'seatNumber': element.seat.seatNumber,
      'seatRow': element.seat.seatRow,
      'seatPrice': element.seat.priceCategory
    });
  }

  displaySectorForm(element: { eventTarget: HTMLElement; sector: Sector }) {
    const updateForm = document.getElementById('updateSectorForm');
    updateForm.style.display = 'inline-block';
    const rectEvent = element.eventTarget.getBoundingClientRect();
    const rectUpdateForm = updateForm.getBoundingClientRect();
    updateForm.style.left = rectEvent.left - rectUpdateForm.width / 2 + rectEvent.width / 2 + 'px';
    updateForm.style.top = rectEvent.top + window.scrollY + rectEvent.height + 20 + 'px';
    // update form with seat parameters
    this.selectedSector = element.sector;
    this.updateSectorForm.setValue({
      'sectorNumber': element.sector.sectorNumber,
      'sectorPrice': element.sector.priceCategory
    });
  }
}
