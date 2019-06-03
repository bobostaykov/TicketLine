import {Component, OnInit} from '@angular/core';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PriceCategory} from '../../../dtos/priceCategory';
import {Hall} from '../../../dtos/hall';
import {Location} from '../../../dtos/location';
import {HallService} from '../../../services/hall/hall.service';
import {LocationService} from '../../../services/location/location.service';

@Component({
  selector: 'app-floorplan-control',
  templateUrl: './floorplan-control.component.html',
  styleUrls: ['./floorplan-control.component.scss']
})
export class FloorplanControlComponent implements OnInit {

  // initialization of new hall entity
  private newHall: Hall = new Hall(null, 'New Hall', null, [], []);
  // contains names and id of all Halls
  private allHalls: Hall[];
  // contains names of specific halls that show up after selecting a specific location
  private halls: Hall[];
  // contains all locations
  private locations: Location[];
  // list of priceCategories to loop through in select fields
  private priceCategories: string[] = Object.keys(PriceCategory);
  // form groups to add seats/sectors and persist new halls to backend
  private addSeatsForm: FormGroup;
  private addSectorsForm: FormGroup;
  private createHallForm: FormGroup;

  constructor(private hallService: HallService, private locationService: LocationService) {
  }

  /**
   * run on initialization of component
   * loads halls and locations from the backend
   * calls initialization of all FormGroups in the component
   */
  ngOnInit(): void {
    // loading halls saved in backend and adding them to the halls  and allHalls array
    this.getAllHalls();
    // loading locations saved in backend and adding them to the locations array
    this.getAllLocations();
    // initializing forms that allow users to add seats and sectors to new Halls
    // as well as persist hall to backend
    this.buildSeatForm();
    this.buildSectorForm();
    this.buildHallForm();
  }

  /**
   * submit method for addSeatsForm
   * adds seats according to parameters entered by user
   * seats are added from seatRowStart to seatRowEnd for numbers seatNumberStart to seatNumberEnd
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
          // only add seat if seat with same number and row does not already exist
          if (!this.getSelectedHall().seats.some(seat => seat.seatRow === row && seat.seatNumber === number)) {
            this.getSelectedHall().seats.push(new Seat(null, number, row, value.seatPrice));
          }
        }
      }
      this.addSeatsForm.reset({
        'seatPrice': PriceCategory.Cheap
      });
      this.changeHallTypeSelection();
    }
  }

  /**
   * submit method for addSectorsForm
   * adds sectors according to parameters entered by user
   * sectors are added with numbers starting from sectorNumberStart to sectorNumberEnd
   * afterwards resets form
   */
  private addSectors(): void {
    if (this.addSectorsForm.valid) {
      const value = this.addSectorsForm.value;
      // if one of the sector number values is null, set equal to other sector number value
      value.sectorNumberStart = value.sectorNumberStart === null ? value.sectorNumberEnd : value.sectorNumberStart;
      value.sectorNumberEnd = value.sectorNumberEnd === null ? value.sectorNumberStart : value.sectorNumberEnd;
      // tslint:disable-next-line:max-line-length
      for (let number = Math.min(value.sectorNumberStart, value.sectorNumberEnd); number <= Math.max(value.sectorNumberStart, value.sectorNumberEnd); number++) {
        // only add sector if a sector with the same number does not already exist
        if (!this.getSelectedHall().sectors.some(sector => sector.sectorNumber === number)) {
          this.getSelectedHall().sectors.push(new Sector(null, number, value.sectorPrice));
        }
      }
      this.addSectorsForm.reset({
        'sectorPrice': PriceCategory.Cheap
      });
      this.changeHallTypeSelection();
    }
  }

  /**
   * another method to submit addSeatsForm
   * deletes seats from seatNumberStart to seatNumberEnd in rows seatRowStart to seatRowEnd
   * afterwards resets form
   */
  private deleteSeats(): void {
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
      this.changeHallTypeSelection();
    }
  }

  /**
   * another method to submit addSectorsForm
   * deletes sectors from sectorNumberStart to sectorNumberEnd
   * afterwards resets form
   */
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
      this.changeHallTypeSelection();
    }
  }

  /**
   * submit method for createHallForm
   * creats new hall with seats or sectors added by user
   * posts hall to backend via hallService
   * afterwards resets form
   */
  private postHall() {
    const values = this.createHallForm.value;
    const hall: Hall = new Hall(
      null,
      values.floorplanName,
      values.locationSelection,
      values.hallSelection.seats,
      values.hallSelection.sectors
    );
    this.hallService.createHall(hall).subscribe(
      createdHall => {
        this.allHalls.push(createdHall);
        this.halls.push(createdHall);
      },
      error => console.log(error)
    );
    this.createHallForm.reset({
      'hallSelection': this.newHall = new Hall(null, 'New SectorPlan', null, [], []),
      'hallType': 'seats'
    });
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
   * initializes createHallForm
   * also notes changes to hall and location selection to set halls locations accordingly
   * if a specific hall with set location is selected its location will also automatically be set in locationSelection
   * if a specific location is selected only halls from given location will be presented to the user
   */
  private buildHallForm() {
    this.createHallForm = new FormGroup({
      'hallSelection': new FormControl(this.newHall, [Validators.required]),
      'locationSelection': new FormControl(null, [Validators.required]),
      'floorplanName': new FormControl(null, [Validators.required]),
      'hallType': new FormControl('seats', [Validators.required])
    });
    const hallSelection = this.createHallForm.get('hallSelection');
    const locationSelection = this.createHallForm.get('locationSelection');
    hallSelection.valueChanges.subscribe(hall => {
      if (hall.location !== null) {
        // necessary because location select tag does not recognize hall.location
        locationSelection.setValue(this.locations.find(location => location.id === hall.location.id), {emitEvent: false});
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
  }

  /**
   * loads all locations from backend via locationService
   */
  private getAllLocations(): void {
    console.log('Loading all locations from backend');
    this.locationService.getAllLocations().subscribe(
      locations => this.locations = locations,
      // TODO: implement proper error handling once globalErrorHandler exists
      error => console.log(error)
    );
  }

  /**
   * helper method
   * returns string representing location
   * @param location for which to return string
   */
  private displayLocation(location: Location): string {
    return location !== null ? location.street + ', ' +
      location.city + ' ' + location.postalCode : '';
  }

  /**
   * helper method
   * returns hall entity currently selected in createHallForm
   */
  private getSelectedHall(): Hall {
    return this.createHallForm.get('hallSelection').value;
  }

  /**
   * called when changes are made to seat or sector arrays of selectedHall
   * disables hallType selection if hall already contains seats or sectors
   * enables hallType selection if hall does not contain seats or sectors anymore
   */
  private changeHallTypeSelection(): void {
    const hallTypeSelection = this.createHallForm.get('hallType');
    if (this.getSelectedHall().seats && this.getSelectedHall().seats.length ||
      this.getSelectedHall().sectors && this.getSelectedHall().sectors.length) {
      hallTypeSelection.disable();
    } else {
      hallTypeSelection.enable();
    }
  }


  // /**
  //  * helper method
  //  * returns true if sectorNumberStart or sectorNumberEnd have been edited and are invalid
  //  * called to determine if error message needs to be displayed
  //  */
  // private sectorNumberErrors(): boolean {
  //   const sectorNumberStart = this.addSectorsForm.get('sectorNumberStart');
  //   const sectorNumberEnd = this.addSectorsForm.get('sectorNumberEnd');
  //   return (sectorNumberStart.dirty || sectorNumberEnd.dirty) && (sectorNumberStart.invalid || sectorNumberEnd.invalid);
  // }
  //
  // /**
  //  * returns true, if inputs seatRowStart or seatRowError have been edited and are invalid
  //  * called to determine if error message needs to be displayed
  //  */
  // private seatRowErrors(): boolean {
  //   const seatRowStart = this.addSeatsForm.get('seatRowStart');
  //   const seatRowEnd = this.addSeatsForm.get('seatRowEnd');
  //   return (seatRowStart.dirty || seatRowEnd.dirty) && (seatRowStart.invalid || seatRowEnd.invalid);
  // }
  //
  // /**
  //  * returns ture, if inputs seatNumberStart or seatNumberEnd have been edited and are invalid
  //  * called to determine if error message needs to be displayed
  //  */
  // private seatNumberErrors(): boolean {
  //   const seatNumberStart = this.addSeatsForm.get('seatNumberStart');
  //   const seatNumberEnd = this.addSeatsForm.get('seatNumberEnd');
  //   return (seatNumberStart.dirty || seatNumberEnd.dirty) && (seatNumberStart.invalid || seatNumberEnd.invalid);
  // }

  // /**
  //  * event handler
  //  * called if floorplan svg is clicked
  //  * if click target was not a seat/sector svg update forms are disabled
  //  * @param event click event
  //  */
  // private onSvgClick(event: Event) {
  //   if ((<HTMLElement>event.target).tagName !== 'path') {
  //     this.disableUpdateForms();
  //   }
  // }

}
