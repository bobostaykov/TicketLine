import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {TicketService} from '../../services/ticket/ticket.service';
import {Ticket} from '../../dtos/ticket';
import {CustomerService} from '../../services/customer/customer.service';
import {EventService} from '../../services/event/event.service';
import {TicketStatus} from '../../datatype/ticket_status';
import {AuthService} from '../../services/auth/auth.service';
import {Tick} from 'ng5-slider/slider.component';

@Component({
  selector: 'app-storno',
  templateUrl: './reservation-and-cancellation.component.html',
  styleUrls: ['./reservation-and-cancellation.component.scss']
})
export class ReservationAndCancellationComponent implements OnInit {
  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private ticketDataReady: boolean = false;
  private error: boolean = false;
  private errorMessage: string = '';
  private cancellationMessage: string = 'Cancellation Successful';
  private saleMessage: string = 'Sale Successful';
  private cantSubmitEmptyMessage: string = 'Cant submit empty list';
  private cantSubmitEmpty = false;
  private saleComplete = false;
  private noSearchResults = false;

  reservationsToSearch: string = '';
  ticketsToSearch: string = '';

  tickets: Ticket[] = [];
  events: Event[];
  stornoArray: number[] = [];
  saleArray: number[] = [];
  cancelTickets: Ticket[] = [];
  saleTickets: Ticket[] = [];
  activeCustomerName: string = '';
  activeEventName: string = '';
  activeTicketNumber: string = '';
  activeReservationNumber: string = '';
  searchByReservationNumber: boolean = false;
  searchByNameAndEvent: boolean = false;
  searchByTicketNumber: boolean = false;
  cancellationCompleted: boolean = false;
  displaySoldTickets = false;



  constructor(private authService: AuthService, private ticketService: TicketService, private customerService: CustomerService, private eventService: EventService) { }
  ngOnInit() {
  }



  /**
   * uses ticketservice to get the ticket by reservationNumber
   * @param reservationsToSearch string with part of the reservation number
   * @param page pagenumber
   */
  private loadReservationsByNumber(reservationsToSearch: string, page: number) {
    this.page = page;
    this.noSearchResults = false;
    this.searchByReservationNumber = true;
    this.searchByNameAndEvent = false;
    this.searchByTicketNumber = false;
    console.log('Get reservations by number: ' + reservationsToSearch);
       this.ticketService.getTicketsByNumber(reservationsToSearch, true, this.page).subscribe(
         result => {
           this.tickets = result['content'];
           this.totalPages = result['totalPages'];
           this.setPagesRange();
         },
         error => {
           this.defaultServiceErrorHandling(error);
         },
         () => {
           if (this.tickets.length === 0){
             this.noSearchResults = true;
             this.ticketDataReady = false;
           } else {
             this.ticketDataReady = true;
             this.displaySoldTickets = false;
           }
         });
  }
  private loadTicketsByNumber(ticketsToSearch: string, page: number) {
    this.page = page;
    this.noSearchResults = false;
    this.searchByTicketNumber = true;
    this.searchByNameAndEvent = false;
    this.searchByTicketNumber = false;
    console.log('Get ticket by number: ' + ticketsToSearch);
    this.ticketService.getTicketsByNumber(ticketsToSearch, false, this.page).subscribe(
      result => {
        this.tickets = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      },
      () => {
        if (this.tickets.length === 0){
          this.noSearchResults = true;
          this.ticketDataReady = false;
        } else {
          this.ticketDataReady = true;
          this.displaySoldTickets = true;
        }
      });
  }



  /**
   * loads a page of tickets filtered by event and customer
   * @param customerToSearch name of the customer
   * @param eventToSearch name of the event
   * @param page required page
   */
  private loadReservationsByCustomerAndEvent(customerToSearch: string, eventToSearch: string, page: number) {
    this.page = page;
    this.noSearchResults = false;
    this.searchByReservationNumber = false;
    this.searchByNameAndEvent = true;
    this.searchByTicketNumber = false;
    console.log('Get reservations by customer: ' + customerToSearch + ' and event: ' + eventToSearch);
    this.ticketService.getReservedTicketsByConsumerAndEvent(customerToSearch, eventToSearch, this.page).subscribe(
      result => {
        this.tickets = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      },
      () => {
        if (this.tickets.length === 0) {
          this.noSearchResults = true;
          this.ticketDataReady = false;
        } else {
          this.ticketDataReady = true;
          this.displaySoldTickets = false;
        }
      }
    );
  }

  /**
   * submits the list of tickets that are to be cancelled to get on concise pdf
   */
  private submitCancellation() {
    console.log('submitting cancellation')
    if (this.cancelTickets.length === 0) {
      this.showCantSubmitEmptyMessage();
    } else {
      this.cancelTickets.forEach(ticket =>  this.stornoArray.push(ticket.id));
      this.ticketService.getCancellationReceiptPdf(this.stornoArray).subscribe(res => {
        const fileURL = URL.createObjectURL(res);
        console.log(fileURL);
        window.open(fileURL, '_blank');
        },
        error => {
          console.log('storno pdf error');
          this.defaultServiceErrorHandling(error);
        },
        () => {
        this.stornoArray = [];
        this.cancelTickets = [];
        this.ticketDataReady = false;
        this.clearSearch();
        this.showCancellationSuccess();
      });
    }
  }

  /**
   * submits a sale, pops up the ticket pdf and clears the forms
   */
  private submitSale() {
    console.log('submitting sale')
      if (this.saleTickets.length === 0) {
        this.showCantSubmitEmptyMessage();
      } else {
        this.saleTickets.forEach(ticket =>  this.saleArray.push(ticket.id));
        this.ticketService.buyReservedTickets(this.saleArray).subscribe(res => {
        }, error => {
          console.log('error while setting to sold');
          this.defaultServiceErrorHandling(error); }, () =>
          this.ticketService.getTicketPdf(this.saleArray).subscribe(res =>  {
              const fileURL = URL.createObjectURL(res);
              console.log(fileURL);
              window.open(fileURL, '_blank');
            },
            error => {
              console.log('sell pdf error');
              this.defaultServiceErrorHandling(error);
            }, () => {
            console.log('sale sucessful');
            this.saleArray = [];
            this.saleTickets = [];
            this.ticketDataReady = false;
            this.displaySoldTickets = false;
            this.clearSearch();
            this.showSaleSuccess(); }));
      }
  }


  /**
   *repeats the last used search with a different page number
   * @param page the page that is to be searched
   */
  private repeatSearch(page: number) {
    console.log('loading new page ' + page);
    if (this.searchByReservationNumber === true) {
      this.loadReservationsByNumber(this.activeReservationNumber, page);
    } else if (this.searchByNameAndEvent === true) {
      this.loadReservationsByCustomerAndEvent(this.activeCustomerName, this.activeEventName, page);
    } else {
      this.loadTicketsByNumber(this.ticketsToSearch, page);
    }
  }

  /**
   *the default error handler
   * @param error the error that is to be handled
   */
  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.ticketCheckReservation !== 'No message available') {
      this.errorMessage = error.error.ticketCheckReservation;
    } else {
      this.errorMessage = error.error.error;
    }
    if (error.error.status === 'BAD_REQUEST') {
      this.errorMessage = error.error.message.replace('400 BAD_REQUEST \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    } else if (error.error.status === 'NOT_FOUND') {
      this.errorMessage = error.error.message.replace('404 NOT_FOUND \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    } else if (error.error.status === 'INTERNAL_SERVER_ERROR') {
      this.errorMessage = error.error.message.replace('404 INTERNAL_SERVER_ERROR \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    }
  }
  /**
   * Determines the page numbers which will be shown in the clickable menu
   */
  private setPagesRange() {
    this.pageRange = []; // nullifies the array
    if (this.totalPages <= 11) {
      for (let i = 0; i < this.totalPages; i++) {
        this.pageRange.push(i);
      }
    } else {
      if (this.page <= 5) {
        for (let i = 0; i <= 10; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page > 5 && this.page < this.totalPages - 5) {
        for (let i = this.page - 5; i <= this.page + 5; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page >= this.totalPages - 5) {
        for (let i = this.totalPages - 10; i < this.totalPages; i++) {
          this.pageRange.push(i);
        }
      }
    }
  }


  /**
   * Sets page number of customers to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
      this.repeatSearch(this.page);
  }

  /**
   * Sets page number of customers to the previous one and calls the last method
   * @param event o handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.repeatSearch(this.page);
    }
  }

  /**
   * Sets page number of customers to the next one and calls the last method
   * @param event to handle
   */
  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.repeatSearch(this.page);
    }
  }

  /**
   * resets all search parameters
   */
  private clearSearch(): void {
    this.searchByNameAndEvent = false;
    this.searchByReservationNumber = false;
    this.reservationsToSearch = '';
    this.ticketsToSearch = '';
    this.activeEventName = '';
    this.activeCustomerName = '';
    this.ticketDataReady = false;
  }

  private setActiveCustomerName(name: string) {
    this.activeCustomerName = name;
  }
  private setActiveEventName(eventName: string) {
    this.activeEventName = eventName;
  }
  private setActiveReservationNumber(reservationNumber: string) {
    this.activeReservationNumber = reservationNumber;
  }

  /**
   * returns true if the ticket status is reservated
   * @param ticket the ticket that is to be checked
   */
  private isReserved(ticket: Ticket): boolean {
    return ticket.status.toString() === TicketStatus.RESERVED.toString();
  }

  /**
   * returns a boolean that gives information if a ticket has a seat (or a sector if false)
   * @param ticket the ticket
   */
  private hasSeatNo(ticket: Ticket): boolean {
    if (ticket.seat !== null && ticket.seat !== undefined) {
      return ticket.seat.seatNumber !== null && ticket.seat.seatNumber !== undefined;
    } else {
      return false;
    }
  }
    /**
     * checks if the ticket has an accessible seat row
     * @param ticket the ticket
     */
  private hasRowNo(ticket: Ticket): boolean {
      if (ticket.seat !== null && ticket.seat !== undefined) {
        return ticket.seat.seatRow !== null && ticket.seat.seatRow !== undefined;
      } else {
        return false;
      }

  }

  /**
   * checks if the the ticket has an accessible sector number
   * @param ticket the ticket
   */
  private hasSectorNo(ticket: Ticket): boolean {
    if (ticket.sector !== null && ticket.sector !== undefined) {
      return ticket.sector.sectorNumber !== null && ticket.sector.sectorNumber !== undefined;
    } else {
      return false;
    }
  }

  /**
   * checks if the ticket has an accessible artist
   * @param ticket the ticket
   */
  private hasArtistName(ticket: Ticket): boolean {
    if (ticket.show.event.artist !== null && ticket.show.event.artist !== undefined) {
      return ticket.show.event.artist.name !== null && ticket.show.event.artist.name !== undefined;
    } else {
      return false;
    }
  }


  /**
   * adds a ticket to the array of tickets that are to be cancelled
   * @param ticket the ticket that is to be added
   */
  private addToCancellation(ticket: Ticket) {
    if (this.saleTickets.length > 0) {
      this.saleTickets = [];
    }
    console.log('added ' + ticket.reservationNo + ' to cancellation');
    if (!this.cancelTickets.includes(ticket)) {
      this.cancelTickets.push(ticket);
    }
  }
  private addToBasket(ticket: Ticket) {
    if (this.cancelTickets.length > 0) {
      this.cancelTickets = [];
    }
    console.log('added ' + ticket.reservationNo + ' to basket');
    if (!this.saleTickets.includes(ticket)) {
      this.saleTickets.push(ticket);
    }
  }
  private dismissFromBasket(ticket: Ticket) {
    console.log('removing ' + ticket.reservationNo);
    if (this.saleTickets.includes(ticket)) {
      this.saleTickets.splice(this.saleTickets.indexOf(ticket), 1);
    } else {
      console.log('not in basket');
    }
  }
  private dismissFromCancellation(ticket: Ticket) {
    console.log('removing ' + ticket.reservationNo);
    if (this.cancelTickets.includes(ticket)) {
      this.cancelTickets.splice(this.cancelTickets.indexOf(ticket), 1);
    } else {
      console.log('not in basket');
    }
}
  /**
   * Error flag will be deactivated, which clears the error message
   */
  private vanishError() {
    this.error = false;
  }
  private showCantSubmitEmptyMessage() {
    this.cantSubmitEmpty = true;
    setTimeout(() => this.cantSubmitEmpty = false, 5000);
  }
  private showCancellationSuccess() {
    this.cancellationCompleted = true;
    setTimeout(() => this.cancellationCompleted = false, 3000);
  }
  private showSaleSuccess() {
    this.saleComplete = true;
    setTimeout(() => this.saleComplete = false, 3000);
  }
}





