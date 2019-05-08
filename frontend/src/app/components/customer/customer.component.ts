import { Component, OnInit } from '@angular/core';
import {Customer} from '../../dtos/customer';
import {CustomerService} from '../../services/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {
  customers: Customer[];

  constructor(private customerService: CustomerService) { }

  /**
   * uses CustomerService to load all customers from backend
   */
  loadCustomers() {
    this.customerService.findAllCustomers().subscribe(
      (customers: Customer[]) => {this.customers = customers; },
      error => {console.log(error); }
    );
  }

  /**
   * Returns true if user is logged in as admin
   * determines whether user is allowed to add customers or update existing customer
   */
  isAdmin() {
    return true;
  }

  ngOnInit() {
    this.loadCustomers();
  }

}
