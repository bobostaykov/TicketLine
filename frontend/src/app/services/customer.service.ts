import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Customer} from '../dtos/customer';
import {Globals} from '../global/globals';
import {HttpClient, HttpParams} from '@angular/common/http';

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

  /**
   * returns all customers found in the backend database as Observable
   */
  findAllCustomers(): Observable<Customer[]> {
    console.log('Getting all customers from backend');
    return this.httpClient.get<Customer[]>(this.customerBaseUri);
  }

  /**
   * Gets all customers from backend that fit search parameters
   * @param customer dto containing search parameters
   */
  searchCustomers(customer: Customer): Observable<Customer[]> {
    console.log('Getting all customers from search parameters: ' + customer);
    const options = {params: new HttpParams()};
    if (customer.id) {
      options.params.set('id', customer.id.toString());
    }
    if (customer.name) {
      options.params.set('name', customer.name);
    }
    if (customer.firstname) {
      options.params.set('firstname', customer.firstname);
    }
    if (customer.email) {
      options.params.set('email', customer.email);
    }
    if (customer.birthday) {
      options.params.set('birthday', customer.birthday.toString());
    }
    return this.httpClient.get<Customer[]>(this.customerBaseUri, options);
  }
}
