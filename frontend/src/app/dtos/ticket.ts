import {Show} from './show';
import {Customer} from './customer';

export class Ticket {
  constructor(
    public id: number,
    public show: Show,
    public customer: Customer,
    public price: number,
    public seatNumber: number,
    public rowNumber: number,
    public sectorNumber: number,
    public status: string) {
  }
}
