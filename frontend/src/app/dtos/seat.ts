import { PriceCategory } from './priceCategory';

export class Seat {
  constructor(
    public id: number,
    public seatNumber: number,
    public seatRow: number,
    public price: PriceCategory) {
  }
}
