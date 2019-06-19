import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../../dtos/customer';
import {CustomerService} from '../../services/customer.service';
import {NgbModal, NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';
import {Seat} from '../../dtos/seat';
import {Sector} from '../../dtos/sector';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent implements OnInit {

  private tickets: Seat[] | Sector[];
  private customerSelection: string = 'Anonymous';
  private selectedCustomer: Customer;
  private anonymousCustomer: Customer = null;
  private foundCustomer: Customer;
  private addedCustomer: Customer;
  private customerFiltered: Customer;
  private customers: Customer[];
  private page;
  private pages: number[];
  @ViewChild('searchCustomersModal') modalReference: ElementRef;

  constructor(private customerService: CustomerService, private modalService: NgbModal) {
  }

  ngOnInit() {
  }

  private getName(): string {
    return this.selectedCustomer ? this.selectedCustomer.firstname + ' ' + this.selectedCustomer.name : '-';
  }

  private getMail(): string {
    return this.selectedCustomer ? this.selectedCustomer.email : '-';
  }

  private getBirthday(): string {
    return this.selectedCustomer ? this.selectedCustomer.birthday : '-';
  }

  private handleSearchSubmission(customer: Customer) {
    this.customerFiltered = customer;
    this.searchCustomers(0);
    const options: NgbModalOptions = {
      size: 'lg'
    };
    this.modalService.open(this.modalReference, options);
  }

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
   * adds newly created customer to backend
   * @param customer to be added
   */
  private addCustomer(customer: Customer) {
    console.log('Adding customer: ' + JSON.stringify(customer));
    this.customerService.createCustomer(customer).subscribe(
      addedCustomer => {
        this.addedCustomer = addedCustomer;
        this.selectedCustomer = customer;
      },
      error => console.log(error)
    );
  }

  /**
   * Sets page number to the chosen i
   */
  private setPage(i) {
    this.page = i;
    if (this.customerFiltered) {
      this.searchCustomers(this.page);
    }
  }

  /**
   * Sets page number to the previous one and calls the last method
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
   * Sets page number to the next one and calls the last method
   */
  private nextPage() {
    if (this.page < this.pages.length - 1) {
      this.page++;
      if (this.customerFiltered) {
        this.searchCustomers(this.page);
      }
    }
  }

  selectCustomer(customer: Customer) {
    this.foundCustomer = customer;
    this.selectedCustomer = customer;
  }

  handleChangeInCustomerSelection(selectionChoice: string) {
    switch (selectionChoice) {
      case 'Anonymous':
        this.selectedCustomer = this.anonymousCustomer;
        break;
      case 'Search Customers':
        this.selectedCustomer = this.foundCustomer;
        break;
      case 'Add Customer':
        this.selectedCustomer = this.addedCustomer;
        break;
    }
  }
}
