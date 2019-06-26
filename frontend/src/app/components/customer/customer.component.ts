import {Component, OnInit} from '@angular/core';
import {Customer} from '../../dtos/customer';
import {CustomerService} from '../../services/customer/customer.service';
import {AuthService} from '../../services/auth/auth.service';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
  styles: ['#errorMessage{z-index: 9999; }']
})
export class CustomerComponent implements OnInit {

  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  private customers: Customer[];
  private customerFiltered: Customer;
  private activeCustomer: Customer;
  private error: boolean = false;
  private errorMessage: string;

  constructor(private customerService: CustomerService, private authService: AuthService) {
  }

  /**
   * Returns true if user is logged in as admin
   * determines whether user is allowed to add customers or update existing customer
   */
  private isAdmin() {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * uses CustomerService to load all customers from backend
   */
  private loadCustomers() {
    console.log('Queries customerService to load all customers');
    this.customerService.findAllCustomers(this.page).subscribe(
        result => {
          this.customers = result['content'];
          this.totalPages = result['totalPages'];
          this.setPagesRange();
      },
      error => {},
      () => { this.dataReady = true; }
    );
  }

  /**
   * Resets all set parameters for customer and page number and calls 'loadCustomers'
   */
  private resetCustomerFilter() {
    this.customerFiltered = undefined;
    this.page = 0;
    this.loadCustomers();
  }

  /**
   * uses CustomerService to search for customers matching parameter
   * @param page number of the page to get
   * @param customer dto that includes search parameters for customerService
   */
  private searchCustomers(customer: Customer, page: number) {
    console.log('Queries customerService to search for customers resembling ' + JSON.stringify(customer));
    this.customerFiltered = customer;
    this.page = page;
    this.customerService.searchCustomers(this.customerFiltered, page).subscribe(
      result => {
        this.customers = result['content'];
        this.totalPages = result['totalPages'];
        this.setPagesRange();
      },
      error => {},
      () => { this.dataReady = true; }
    );
  }

  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    if (this.customerFiltered !== undefined) {
      this.searchCustomers(this.customerFiltered, this.page);
    } else {
      this.loadCustomers();
    }
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event o handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      if (this.customerFiltered !== undefined) {
        this.searchCustomers(this.customerFiltered,  this.page);
      } else {
        this.loadCustomers();
      }
    }
  }

  /**
   * Sets page number to the next one and calls the last method
   * @param event to handle
   */
  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.totalPages - 1) {
      this.page++;
      if (this.customerFiltered !== undefined) {
        this.searchCustomers(this.customerFiltered , this.page);
      } else {
        this.loadCustomers();
      }
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
   * sets specific customer as variable activeCustomer
   * @param customer to be set as activeCustomer
   */
  private setActiveCustomer(customer: Customer) {
    this.activeCustomer = customer; // TODO?   Object.assign(this.activeCustomer, customer);
  }

  /**
   * uses CustomerService to update customer passed as param, should be activeCustomer
   * @param customer to be updated
   */
  private updateCustomer(customer: Customer) {
    console.log('Updates customer with id ' + customer.id + ' to ' + JSON.stringify(customer));
    Object.assign(this.activeCustomer, customer);
    this.customerService.updateCustomer(customer);
    this.loadCustomers();
  }

  /**
   * adds newly created customer to backend
   * @param customer to be added
   */
  private addCustomer(customer: Customer) {
    console.log('Adding customer: ' + JSON.stringify(customer));
    this.customerService.createCustomer(customer).subscribe(
      addedCustomer => this.customers.push(addedCustomer),
      error => { this.loadCustomers(); }
    );
  }

  /**
   * deactivates error flag, stops displaying error to user
   */
  private vanishError() {
    this.error = false;
  }

  /**
   * runs after component initialization
   * loads all customers from backend for tutorial purposes
   * TODO: either implement lazy loading or stop loading all customers
   */
  ngOnInit() {
    console.log('Customer Component was initialized!');
    this.loadCustomers();
  }

}
