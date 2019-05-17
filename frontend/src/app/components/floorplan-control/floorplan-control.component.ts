import {Component, OnInit} from '@angular/core';
import {Seat} from '../../dtos/seat';
import {PriceCategory} from '../../dtos/priceCategory';

@Component({
  selector: 'app-floorplan-control',
  templateUrl: './floorplan-control.component.html',
  styleUrls: ['./floorplan-control.component.scss']
})
export class FloorplanControlComponent implements OnInit {
  seats: Seat[] = [
    new Seat(1, 1, 1, PriceCategory.Cheap),
    new Seat(1, 2, 1, PriceCategory.Cheap),
    new Seat(1, 3, 1, PriceCategory.Cheap),
    new Seat(1, 4, 1, PriceCategory.Cheap),
    new Seat(1, 5, 1, PriceCategory.Cheap),
    new Seat(1, 6, 1, PriceCategory.Cheap),
    new Seat(1, 7, 1, PriceCategory.Cheap),
    new Seat(1, 8, 1, PriceCategory.Cheap),
    new Seat(1, 9, 1, PriceCategory.Cheap),
    new Seat(1, 10, 1, PriceCategory.Cheap),
  ];
  private seatFormModel = {
    seatRowStart: null,
    seatRowEnd: null,
    seatNumberStart: null,
    seatNumberEnd: null
  };

  ngOnInit(): void {
  }

  private addSeats() {
    const seatRowStart = Math.min(this.seatFormModel.seatRowStart, this.seatFormModel.seatRowEnd);
    const seatRowEnd = Math.max(this.seatFormModel.seatRowStart, this.seatFormModel.seatRowEnd);
    const seatNumberStart = Math.min(this.seatFormModel.seatNumberStart, this.seatFormModel.seatNumberEnd);
    const seatNumberEnd = Math.max(this.seatFormModel.seatNumberStart, this.seatFormModel.seatNumberEnd);
    for (let row = seatRowStart; row <= seatRowEnd; row++) {
      for (let number = seatNumberStart; number < seatNumberEnd; number++) {
        if (! this.seats.some(seat => seat.seatRow === row && seat.seatNumber === number)) {
          this.seats.push(new Seat(null, number, row, PriceCategory.Average));
        }
      }
    }
  }
}
