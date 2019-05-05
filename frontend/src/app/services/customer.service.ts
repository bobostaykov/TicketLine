import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Customer} from "../dtos/customer";
import {Globals} from '../global/globals';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private customerBaseUri: string = this.globals.backendUri + '/customers';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Persists customer to the backend
   * @param customer to persist
   */
  createCustomer(customer: Customer): Observable<Customer> {
    console.log('Create customer with name ' + customer.name);
    return this.httpClient.post<Customer>(this.customerBaseUri, customer);
  }
}
