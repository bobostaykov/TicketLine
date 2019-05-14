import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
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

  private onHoverSelect: boolean = true;
  private hoveredSeat: Seat = new Seat(null, null, null, null);
  private updatedSeat: Seat = new Seat(null, null, null, null);
  private svgPosX;
  private svgPosY;
  private viewboxPosX = 0;
  private viewboxPosY = 0;
  private viewboxWidth = 500;
  private viewbox: string = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;

  constructor() {
  }

  private hoverSeat(seat: Seat) {
    if (this.onHoverSelect) {
      this.hoveredSeat = seat;
      Object.assign(this.updatedSeat, this.hoveredSeat);
    }
  }

  private selectSeat(seat: Seat) {
    this.onHoverSelect = false;
    this.hoveredSeat = seat;
    Object.assign(this.updatedSeat, this.hoveredSeat);
  }

  private deselectSeat(event) {
    if (event.target.tagName !== 'path') {
      this.onHoverSelect = true;
    }
  }

  private getPath(seat: Seat): string {
    const xPos: number = (seat.seatNumber - 1) * 1.2 * 10 + Math.floor(seat.seatNumber / 15) * 10;
    const yPos: number = seat.seatRow * 1.2 * 10 + Math.floor(seat.seatRow / 10) * 10;
    return 'M' + xPos + ' ' + yPos + 'h 10 v 10 h -10 Z';
  }

  private getColor(seat: Seat): string {
    return seat.price === PriceCategory.CHEAP ? '#2fb207' : (seat.price === PriceCategory.AVERAGE ? '#129ded' : '#db0611');
  }

  private zoom(event) {
    event.preventDefault();
    this.viewboxWidth = event.deltaY > 0 ? this.viewboxWidth * 1.05 : this.viewboxWidth / 1.05;
    this.viewboxWidth = this.viewboxWidth < 300 ? 300 : (this.viewboxWidth > 1310 ? 1310 : this.viewboxWidth);
    this.viewboxPosX = event.pageX - this.svgPosX - this.viewboxWidth / 2;
    this.viewboxPosY = event.pageY - this.svgPosY - this.viewboxWidth / 2;
    this.viewboxPosX = this.viewboxPosX < 0 ? 0 : this.viewboxPosX;
    this.viewboxPosY = this.viewboxPosY < 0 ? 0 : this.viewboxPosX;
    console.log(this.viewboxPosX + ', ' + this.viewboxPosY);
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  }

  ngOnInit() {
    const rect = document.getElementById('floorplan').getBoundingClientRect();
    this.svgPosX = rect.left;
    this.svgPosY = rect.top;
  }
}
