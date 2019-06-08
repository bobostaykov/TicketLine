import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {PriceCategory} from '../../../dtos/priceCategory';

@Component({
  selector: 'app-floorplan-update',
  templateUrl: './floorplan-update.component.html',
  styleUrls: ['./floorplan-update.component.scss']
})
export class FloorplanUpdateComponent implements OnInit, OnChanges {

  // selected seat or sector for which to display update dialog box
  private selectedElement: Seat | Sector = new Seat(null, null, null, null);
  // html representation of selected element
  private htmlElement: HTMLElement;
  // list of priceCategories to loop through in select fields
  private priceCategories: string[] = Object.keys(PriceCategory);

  constructor() {
  }

  //
  // private displayUpdateForm(element: Seat | Sector): void {
  //   const updateForm = document.getElementById('updateForm');
  //   const rectForm = updateForm.getBoundingClientRect();
  //   const rectSvg = document.getElementsByTagName('svg')[0].getBoundingClientRect();
  // }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedElement']) {
    }


    // /**
    //  * displays div containing updateSeatForm at position in page where seat to update is located
    //  * @param element parameter passed form Floorplan component. Contains both HTML element that was selected as well as
    //  * seat object it represents
    //  */
    // displaySeatForm(element: { eventTarget: HTMLElement; seat: Seat }) {
    //   const updateForm = document.getElementById('updateSeatForm');
    //   updateForm.style.display = 'inline-block';
    //   const rectEvent = element.eventTarget.getBoundingClientRect();
    //   const rectUpdateForm = updateForm.getBoundingClientRect();
    //   updateForm.style.left = rectEvent.left - rectUpdateForm.width / 2 + rectEvent.width / 2 + 'px';
    //   updateForm.style.top = rectEvent.top + window.scrollY + rectEvent.height + 20 + 'px';
    //   // update form with seat parameters and set selectedSeat
    //   this.selectedSeat = element.seat;
    //   this.updateSeatForm.setValue({
    //     'seatNumber': element.seat.seatNumber,
    //     'seatRow': element.seat.seatRow,
    //     'seatPrice': element.seat.priceCategory
    //   });
    // }
    //
    // /**
    //  * displays div contatining updateSectorForm at position in page where sector to update is located
    //  * @param element parameter passed from Floorplan component. Contains both HTML element that was selected as well as sector object
    //  * it represents
    //  */
    // displaySectorForm(element: { eventTarget: HTMLElement; sector: Sector }) {
    //   const updateForm = document.getElementById('updateSectorForm');
    //   updateForm.style.display = 'inline-block';
    //   const rectEvent = element.eventTarget.getBoundingClientRect();
    //   const rectUpdateForm = updateForm.getBoundingClientRect();
    //   updateForm.style.left = rectEvent.left - rectUpdateForm.width / 2 + rectEvent.width / 2 + 'px';
    //   updateForm.style.top = rectEvent.top + window.scrollY + rectEvent.height + 20 + 'px';
    //   // update form with seat parameters
    //   this.selectedSector = element.sector;
    //   this.updateSectorForm.setValue({
    //     'sectorNumber': element.sector.sectorNumber,
    //     'sectorPrice': element.sector.priceCategory
    //   });
    // }
    //
    // /**
    //  * submit method for updateSeatForm
    //  * updates seats with specified parameters if seat with the same number and row does not already exist
    //  * afterwards hides form
    //  */
    // private updateSeat() {
    //   const values = this.updateSeatForm.value;
    //   if (!this.getSelectedHall().seats.some(seat => seat !== this.selectedSeat && seat.seatNumber === values.seatNumber &&
    //     seat.seatRow === values.seatRow)) {
    //     this.selectedSeat.seatNumber = values.seatNumber;
    //     this.selectedSeat.seatRow = values.seatRow;
    //     this.selectedSeat.priceCategory = values.seatPrice;
    //     this.seatAlreadyExists = false;
    //     this.disableUpdateForms();
    //   } else {
    //     this.seatAlreadyExists = true;
    //   }
    // }
    //
    // /**
    //  * submit method for updateSectorForm
    //  * updates sector with specified parameters if sector with the same number does not already exist
    //  * afterwards hides form
    //  */
    // private updateSector() {
    //   const values = this.updateSectorForm.value;
    //   if (!this.getSelectedHall().sectors.some(sector => sector !== this.selectedSector &&
    //     sector.sectorNumber === values.sectorNumber)) {
    //     this.selectedSector.sectorNumber = values.sectorNumber;
    //     this.selectedSector.priceCategory = values.sectorPrice;
    //     this.sectorAlreadyExists = false;
    //     this.disableUpdateForms();
    //   } else {
    //     this.sectorAlreadyExists = true;
    //   }
    // }
    //
    // /**
    //  * another method to submit updateSeatForm
    //  * deletes selected seat instead of updating it and afterwards hides the form
    //  */
    // private deleteSeat() {
    //   const seats: Seat[] = this.createHallForm.get('hallSelection').value.seats;
    //   seats.splice(seats.indexOf(this.selectedSeat), 1);
    //   this.disableUpdateForms();
    // }
    //
    // /**
    //  * another method to submit updateSectorForm
    //  * deletes selected sector instead of updating it and afterwards hides the form
    //  */
    // private deleteSector() {
    //   const sectors: Sector[] = this.createHallForm.get('hallSelection').value.sectors;
    //   sectors.splice(sectors.indexOf(this.selectedSector), 1);
    //   this.disableUpdateForms();
    // }
    //
    // /**
    //  * hides both updateSeatForm and updateSectorForm
    //  */
    // private disableUpdateForms() {
    //   document.getElementById('updateSeatForm').style.display = 'none';
    //   document.getElementById('updateSectorForm').style.display = 'none';
    // }

  }
}
