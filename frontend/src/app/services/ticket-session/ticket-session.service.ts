import {Injectable} from '@angular/core';
import {Seat} from '../../dtos/seat';
import {Sector} from '../../dtos/sector';
import {Show} from '../../dtos/show';
import {Customer} from '../../dtos/customer';
import {TicketStatus} from '../../datatype/ticket_status';

@Injectable({
  providedIn: 'root'
})
/**
 * service storing active ticket information during user session
 */
export class TicketSessionService {
  // contain seats and sectors that tickets are associated with
  private ticket_seats: Seat[] = [];
  private ticket_sectors: Sector[] = [];
  // show associated with tickets
  private show: Show;
  // customers associated with tickets
  private customer: Customer;
  // ticketStatus - whether tickets should be bought or reserved
  ticketStatus: TicketStatus = TicketStatus.SOLD;

  constructor() {
  }

  /**
   * saves a seat to the service unless it already existed there before
   * since tickets are always sold for just one show tickets can never be sold for seats and sectors at the same time
   * therefore ticket_sectors array is flushed
   * @param seat to be saved
   */
  saveSeatTicket(seat: Seat) {
    if (this.show && !this.ticket_seats.some(s => seat === s)) {
      seat.price = this.show.pricePattern.priceMapping[seat.priceCategory];
      this.ticket_sectors = this.ticket_sectors.length > 0 ? [] : this.ticket_sectors;
      this.ticket_seats.push(seat);
    }
  }

  /**
   * saves a sector to the service unless it already existed there before
   * since tickets are always sold for just one show tickets can never be sold for seats and sectors at the same time
   * therefore ticket_seats array is flushed
   * @param sector to be saved
   */
  saveSectorTicket(sector: Sector) {
    if (this.show && !this.ticket_sectors.some(s => sector === s)) {
      sector.price = this.show.pricePattern.priceMapping[sector.priceCategory];
      this.ticket_seats = this.ticket_seats.length > 0 ? [] : this.ticket_seats;
      this.ticket_sectors.push(sector);
    }
  }

  /**
   * changes show for which tickets are sold and flushes seat and sector ticket arrays because they do not contain seats/sectors
   * associated with the selected show
   * @param show for which tickets are sold
   */
  changeShow(show: Show) {
    this.ticket_seats = [];
    this.ticket_sectors = [];
    this.show = show;
  }

  /**
   * saves customer to whom tickets are sold in the service
   * @param customer to be saved
   */
  saveCustomer(customer: Customer) {
    this.customer = customer;
  }

  /**
   * saves ticketstatus to the service - whether tickets are sold or just reserved
   * @param status of ticket purchase to be saved
   */
  saveTicketStatus(status: TicketStatus) {
    this.ticketStatus = status;
  }

  /**
   * deletes a ticket from either the seats or sectors array depending on which one is filled and must therefore contain the ticket
   * @param ticket to be deleted
   */
  deleteTicket(ticket: Seat | Sector) {
    if (this.ticket_seats.length > 0) {
      this.ticket_seats.splice(this.ticket_seats.indexOf(ticket as Seat), 1);
    } else {
      this.ticket_sectors.splice(this.ticket_sectors.indexOf(ticket as Sector), 1);
    }
  }

  /**
   * getter; returns seat tickets array
   */
  getSeatTickets(): Seat[] {
    return this.ticket_seats;
  }

  /**
   * getter; returns sector tickets array
   */
  getSectorTickets(): Sector[] {
    return this.ticket_sectors;
  }

  /**
   * returns whichever ticket array (seats or sectors) is currently filled
   */
  getTickets(): Seat[] | Sector[] {
    return this.ticket_seats.length > 0 ? this.ticket_seats : this.ticket_sectors;
  }

  /**
   * getter; returns ticket show
   */
  getShow(): Show {
    return this.show;
  }

  /**
   * getter; returns ticket customer
   */
  getCustomer(): Customer {
    return this.customer;
  }

  /**
   * getter; returns ticket status
   */
  getTicketStatus(): TicketStatus {
    return this.ticketStatus;
  }
}
