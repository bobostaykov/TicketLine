import {Component, OnInit} from '@angular/core';
import {Customer} from '../../dtos/customer';
import {CustomerService} from '../../services/customer.service';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styles: ['#errorMessage{z-index: 9999; }']
})
export class CustomerComponent implements OnInit {
  private customers: Customer[];
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
    this.customerService.findAllCustomers().subscribe(
      (customers: Customer[]) => {
        this.customers = customers;
      },
      error => this.defaultServiceErrorHandling(error)
    );
  }

  /**
   * uses CustomerService to search for customers matching parameter
   * @param customer dto that includes search parameters for customerService
   */
  private searchCustomers(customer: Customer) {
    console.log('Queries customerService to search for customers resembling ' + JSON.stringify(customer));
    this.customerService.searchCustomers(customer).subscribe(
      (customers: Customer[]) => {
        this.customers = customers;
      },
      error => this.defaultServiceErrorHandling(error)
    );
  }

  /**
   * sets specific customer as variable activeCustomer
   * @param customer to be set as activeCustomer
   */
  private setActiveCustomer(customer: Customer) {
    this.activeCustomer = customer;
  }

  /**
   * uses CustomerService to update customer passed as param, should be activeCustomer
   * @param customer to be updated
   */
  private updateCustomer(customer: Customer) {
    console.log('Updates custoemr with id ' + customer.id + ' to ' + JSON.stringify(customer));
    Object.assign(this.activeCustomer, customer);
    this.customerService.updateCustomer(customer);
  }

  /**
   * adds newly created customer to backend
   * @param customer to be added
   */
  private addCustomer(customer: Customer) {
    console.log('Adding customer: ' + JSON.stringify(customer));
    this.customerService.createCustomer(customer).subscribe(
      addedCustomer => this.customers.push(addedCustomer),
      error => this.defaultServiceErrorHandling(error)
    );
  }

  /**
   * activates error flag and sets error message to display to user
   * @param error that was encountered, includes error message
   */
  /* PINO: extended if clause in order to get into else if no error.error.news is available*/
  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.news !== 'No message available' && error.error.news !== '' && error.error.news) {
      this.errorMessage = error.error.news;
    } else {
      this.errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
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
