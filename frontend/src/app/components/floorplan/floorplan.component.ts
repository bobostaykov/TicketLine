import {Component, Input, OnInit} from '@angular/core';
import {Seat} from '../../dtos/seat';
import {Sector} from '../../dtos/sector';
import {PriceCategory} from '../../dtos/priceCategory';

@Component({
  selector: 'app-floorplan',
  templateUrl: './floorplan.component.html',
  styleUrls: ['./floorplan.component.scss']
})
export class FloorplanComponent implements OnInit {
  @Input() private seats?: Seat[];
  @Input() private sectors?: Sector[];
  private priceCategories: string[] = Object.keys(PriceCategory);
  private viewboxPosX = 0;
  private viewboxPosY = 0;
  private viewboxWidth = 500;
  private viewbox: string = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  private selectedElement: Seat | Sector;
  private updatedElement: Seat | Sector;

  constructor() {
  }

  /**
   * gets string for d attribute of svg path element depicting a seat
   * @param seat for which to return path
   */
  private getPath(seat: Seat): string {
    const xPos: number = (seat.seatNumber - 1) * 1.2 * 10 + Math.floor(seat.seatNumber / 15) * 10;
    const yPos: number = seat.seatRow * 1.2 * 10 + Math.floor(seat.seatRow / 10) * 10;
    return 'M' + xPos + ' ' + yPos + 'h 10 v 10 h -10 Z';
  }

  /**
   * get color of seat passed as parameter
   * seat color depends on the price category of the seat
   * @param seat for which to return color
   */
  private getColor(seat: Seat): string {
    return seat.price === PriceCategory.Cheap ? '#2fb207' : (seat.price === PriceCategory.Average ? '#129ded' : '#db0611');
  }

  /**
   * allows users to zoom in by adjusting svg viewbox
   * @param event mousewheel event used to zoom
   */
  private zoom(event) {
    event.preventDefault();
    this.viewboxWidth = event.deltaY > 0 ? this.viewboxWidth * 1.05 : this.viewboxWidth / 1.05;
    this.viewboxWidth = this.viewboxWidth < 300 ? 300 : (this.viewboxWidth > 1310 ? 1310 : this.viewboxWidth);
    const rect = document.getElementById('floorplan').getBoundingClientRect();
    this.viewboxPosX = event.pageX - rect.left - this.viewboxWidth / 2;
    this.viewboxPosY = event.pageY - rect.top - this.viewboxWidth / 2;
    this.viewboxPosX = this.viewboxPosX < 0 ? 0 : this.viewboxPosX;
    this.viewboxPosY = this.viewboxPosY < 0 ? 0 : this.viewboxPosX;
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  }

  /**
   * displays form to update a seat
   * @param seat to be updated
   * @param e mouse click event used to determine position of update form
   */
  private displayUpdateForm(seat: Seat, e: Event) {
    const updateForm = document.getElementById('updateForm');
    updateForm.style.display = 'inline-block';
    const rectEvent = (e.target as Element).getBoundingClientRect();
    const rectUpdateForm = updateForm.getBoundingClientRect();
    updateForm.style.left = rectEvent.left - rectUpdateForm.width / 2 + rectEvent.width / 2 + 'px';
    updateForm.style.top = rectEvent.top + rectEvent.height + 20 + 'px';
    // update form with seat parameters
    this.selectedElement = seat;
    Object.assign(this.updatedElement, seat);
  }

  /**
   * disables update form
   * called when clicking inside the svg but not on a path element or the form itself
   * @param e click event
   */
  private disableUpdateForm(e: Event) {
    if (e.target['tagName'] !== 'path') {
      const updateForm = document.getElementById('updateForm');
      updateForm.style.display = 'none';
    }
  }

  /**
   * updates selected seat with parameters passed into form
   */
  updateSeat() {
    Object.assign(this.selectedElement, this.updatedElement);
  }

  ngOnInit() {
    if (this.seats !== undefined) {
      this.updatedElement = new Seat(null, null, null, null);
    } else if (this.sectors !== undefined) {
      this.updatedElement = new Sector(null, null, null);
    } else {
      this.updatedElement = null;
    }
  }
}
