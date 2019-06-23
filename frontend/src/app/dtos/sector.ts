import {PriceCategory} from './priceCategory';
import {TicketStatus} from '../datatype/ticket_status';

export class Sector {
  constructor(
    public id: number,
    public sectorNumber: number,
    public priceCategory: PriceCategory,
    public price: number,
    public ticketStatus: TicketStatus
  ) {
  }
}
