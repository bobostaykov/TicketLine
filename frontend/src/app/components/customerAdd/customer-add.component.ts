import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Customer} from "../../dtos/customer";
import {CustomerService} from "../../services/customer.service";
import {composeValidators} from "@angular/forms/src/directives/shared";

@Component({
  selector: 'app-customer',
  templateUrl: './customer-add.component.html',
  styleUrls: ['./customer-add.component.scss']
})
export class CustomerAddComponent implements OnInit {

  error: boolean = false;
  errorMessage: string = '';
  customerForm: FormGroup;
  submitted: boolean = false;

  constructor(private customerService: CustomerService, private formBuilder: FormBuilder) {
    this.customerForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      firstname: ['', [Validators.required]],
      email: ['', [Validators.email, Validators.required]],
      birthday: ['', [Validators.required]]
    });
  }

  ngOnInit() {
  }

  /**
   * Starts form validation and builds a customer dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addCustomer() {
    this.submitted = true;
    if (this.customerForm.valid) {
      const customer: Customer = new Customer(null,
        this.customerForm.controls.name.value,
        this.customerForm.controls.firstname.value,
        this.customerForm.controls.email.value,
        this.customerForm.controls.birthday.value);
      this.createCustomer(customer);
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Sends customer creation request
   * @param customer the customer which should be created
   */
  createCustomer(customer: Customer) {
    this.customerService.createCustomer(customer).subscribe(
      () => {
        // TODO: delete console message and redirect to customer view of just created customer(?)
        console.log('CUSTOMER CREATION SUCCESSFUL');

      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.error;
    }
  }

  private clearForm() {
    this.customerForm.reset();
    this.submitted = false;
  }

}
