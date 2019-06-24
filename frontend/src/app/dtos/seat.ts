import {PriceCategory} from './priceCategory';
import {TicketStatus} from '../datatype/ticket_status';

export class Seat {
  constructor(
    public id: number,
    public seatNumber: number,
    public seatRow: number,
    public priceCategory: PriceCategory,
    public price: number,
    public ticketStatus: TicketStatus) {
  }
}
