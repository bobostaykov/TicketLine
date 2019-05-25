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
  private createHallForm: FormGroup;

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
  }

  /**
   * called on submit of addSeatsForm
   * adds seats according to parameters entered by user
   * afterwards resets form
   */
  private addSeats(): void {
    if (this.addSeatsForm.valid) {
      const value = this.addSeatsForm.value;
      // if one of the seat row or number values is null, set equal to other seat row/number value
      value.seatRowStart = value.seatRowStart === null ? value.seatRowEnd : value.seatRowStart;
      value.seatRowEnd = value.seatRowEnd === null ? value.seatRowStart : value.seatRowEnd;
      value.seatNumberStart = value.seatNumberStart === null ? value.seatNumberEnd : value.seatNumberStart;
      value.seatNumberEnd = value.seatNumberEnd === null ? value.seatNumberStart : value.seatNumberEnd;
      // determine which value is min and which is max to run loop correctly afterwards
      value.seatRowStart = Math.min(value.seatRowStart, value.seatRowEnd);
      value.seatRowEnd = Math.max(value.seatRowStart, value.seatRowEnd);
      value.seatNumberStart = Math.min(value.seatNumberStart, value.seatNumberEnd);
      value.seatNumberEnd = Math.max(value.seatNumberStart, value.seatNumberEnd);
      for (let row = value.seatRowStart; row <= value.seatRowEnd; row++) {
        for (let number = value.seatNumberStart; number <= value.seatNumberEnd; number++) {
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
      // determine which value is min and which is max to run loop correctly afterwards
      value.sectorNumberStart = Math.min(value.sectorNumberStart, value.sectorNumberEnd);
      value.sectorNumberEnd = Math.max(value.sectorNumberStart, value.sectorNumberEnd);
      for (let number = value.sectorNumberStart; number <= value.sectorNumberEnd; number++) {
        if (!this.getSelectedHall().sectors.some(sector => sector.sectorNumber === number)) {
          this.getSelectedHall().sectors.push(new Sector(null, number, value.sectorPrice));
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
    });
    locationSelection.valueChanges.subscribe(location => {
      if (location !== null) {
        this.halls = this.allHalls.filter(hall => hall.location.id === location.id);
      } else {
        this.halls = this.allHalls;
      }
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
      location.city + location.postalCode : '';
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
    console.log(hall);
    this.hallService.createHall(hall).subscribe();
    this.createHallForm.reset({
      'hallSelection': (values.hallSelection === this.newSeatPlan ? this.newSeatPlan = new Hall(null, 'New SeatPlan', null, [], null) :
        this.newSectorPlan = new Hall(null, 'New SectorPlan', null, null, []))
    });
  }
}
