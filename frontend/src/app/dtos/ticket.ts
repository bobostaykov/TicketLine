import {Show} from './show';
import {Customer} from './customer';

export class Ticket {
  constructor(
    public id: number,
    public show: Show,
    public price: number,
    public customer: Customer,
    public seatNumber: number,
    public seatRow: number,
    public sectorNumber: number,
    public status: ticketStatus) {
  }
}

enum ticketStatus {
  'BUY',
  'RESERVATION'
}
