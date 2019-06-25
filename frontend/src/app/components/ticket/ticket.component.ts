import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../../dtos/customer';
import {CustomerService} from '../../services/customer/customer.service';
import {NgbModal, NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';
import {TicketSessionService} from '../../services/ticket-session/ticket-session.service';
import {TicketStatus} from '../../datatype/ticket_status';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent implements OnInit {

  // user can choose between selling tickets to an anonymous customer, add a new customer to the system or buy for an existing customer
  // customerSelection, anonymousCustomer, foundCustomer and addedCustomer are used to choose between these different approaches and
  // persist results
  private customerSelection: string = 'Anonymous';
  private anonymousCustomer: Customer = null;
  private foundCustomer: Customer;
  private addedCustomer: Customer;
  // returned from customerSearch. Used to send queries to backend when page is changed
  private customerFiltered: Customer;
  // array storing the results of customer search
  // noinspection JSMismatchedCollectionQueryUpdate
  private customers: Customer[];
  // page elements are used for pagination - to switch between different pages and send new queries to backend
  private page;
  private pages: number[];
  // statusEnum is necessary to access TicektStatus enum in HTML templating
  private statusEnum = TicketStatus;
  @ViewChild('searchCustomersModal') modalReference: ElementRef;

  constructor(private customerService: CustomerService,
              private modalService: NgbModal,
              private ticketSession: TicketSessionService) {
  }

  ngOnInit() {
    this.foundCustomer = this.ticketSession.getCustomer();
  }

  /**
   * adds newly created customer to backend
   * @param customer to be added
   */
  private addCustomer(customer: Customer) {
    console.log('Adding customer: ' + JSON.stringify(customer));
    this.customerService.createCustomer(customer).subscribe(
      addedCustomer => {
        this.addedCustomer = addedCustomer;
        this.ticketSession.saveCustomer(addedCustomer);
      },
      error => console.log(error)
    );
  }

  /**
   * called after searching via customerDialog component
   * sends search request via searchCustomers method and customer service
   * also opens modal displaying search results
   * @param customer containing search parameters
   */
  private handleSearchSubmission(customer: Customer) {
    this.customerFiltered = customer;
    this.searchCustomers(0);
    const options: NgbModalOptions = {
      size: 'lg'
    };
    this.modalService.open(this.modalReference, options);
  }

  /**
   * gets one pageable of customers from the backend using the search parameters saved in filteredCustomers variable
   * @param page of results that should be returned from the backend
   */
  private searchCustomers(page: number): void {
    console.log('Queries customerService to search for customers resembling ' + JSON.stringify(this.customerFiltered));
    this.page = page;
    this.customerService.searchCustomers(this.customerFiltered, page).subscribe(
      result => {
        this.customers = result['content'];
        this.pages = new Array(result['totalPages']);
      },
      error => console.log(error),
    );
  }

  /**
   * selects a given customer within modal of search results
   * customer is then saved as foundCustomer
   * @param customer that was selected
   */
  selectCustomer(customer: Customer) {
    this.foundCustomer = customer;
    this.ticketSession.saveCustomer(customer);
  }

  /**
   * Sets page number to the chosen i and calls search method
   * @param i number of page that should be searched for
   */
  private setPage(i) {
    this.page = i;
    if (this.customerFiltered) {
      this.searchCustomers(this.page);
    }
  }

  /**
   * Sets page number to the previous one and calls search method
   */
  private previousPage() {
    if (this.page > 0) {
      this.page--;
      if (this.customerFiltered) {
        this.searchCustomers(this.page);
      }
    }
  }

  /**
   * Sets page number to the next one and calls search method
   */
  private nextPage() {
    if (this.page < this.pages.length - 1) {
      this.page++;
      if (this.customerFiltered) {
        this.searchCustomers(this.page);
      }
    }
  }

  /**
   * switches between different modes of customer selection and saves customer that was found/added/anonymous to ticketSession according
   * to selectionChoice
   * @param selectionChoice representing customer type. Tells system whether to use added customer, found customer or
   * anonymous customer
   */
  handleChangeInCustomerSelection(selectionChoice: string) {
    switch (selectionChoice) {
      case 'Anonymous':
        this.ticketSession.saveCustomer(this.anonymousCustomer);
        break;
      case 'Search Customers':
        this.ticketSession.saveCustomer(this.foundCustomer);
        break;
      case 'Add Customer':
        this.ticketSession.saveCustomer(this.addedCustomer);
        break;
    }
  }


  /**
   * returns customer first and last name or '-' to display in HTML
   * depends on whether a customer has or has not already been selected
   */
  private getName(): string {
    return this.ticketSession.getCustomer() ? this.ticketSession.getCustomer().firstname + ' '
      + this.ticketSession.getCustomer().name : '-';
  }

  /**
   * returns customer email or '-' to display in HTML
   * depends on whether a customer has or has not already been selected
   */
  private getMail(): string {
    return this.ticketSession.getCustomer() ? this.ticketSession.getCustomer().email : '-';
  }

  /**
   * returns customer birthday or '-' to display in HTML
   * depends on whether a customer has or has not already been selected
   */
  private getBirthday(): string {
    return this.ticketSession.getCustomer() ? this.ticketSession.getCustomer().birthday : '-';
  }

  /**
   * returns name of button to proceed to last step of the process to buy tickets
   * 'Buy Tickets' or 'Reserve Tickets' depending on ticketStatus selected by the user
   */
  private getButtonName(): string {
    return this.ticketSession.getTicketStatus() === TicketStatus.SOLD ? 'Buy Tickets' : 'Reserve Tickets';
  }

  /**
   * decides whether button to continue to ticketCheckComponent is enabled
   * depends on whether at least one ticket has been chosen and whether customer was selected
   */
  private setButtonDisabled(): boolean {
    return this.ticketSession.getTickets().length < 1 || !this.ticketSession.getShow() ||
      (!this.ticketSession.getCustomer() && this.customerSelection !== 'Anonymous');
  }

}
