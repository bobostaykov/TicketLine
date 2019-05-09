import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output, SimpleChanges
} from '@angular/core';
import {Customer} from '../../dtos/customer';

@Component({
  selector: 'app-customer-dialog',
  templateUrl: './customer-dialog.component.html',
})
export class CustomerDialogComponent implements OnInit, OnChanges {

  @Input() title: string;
  @Input() customer: Customer;
  @Output() submitCustomer = new EventEmitter<Customer>();
  private customerModel: Customer = new Customer(null, null, null, null, null);
  private requiredValues: boolean;

  constructor() {
  }

  /**
   * on submission of form emit event with customer to listening parent
   */
  private onSubmit() {
    this.submitCustomer.emit(this.customerModel);
  }

  /**
   * runs after initialization
   * checks which customer values are required to be entered
   */
  ngOnInit() {
    this.requiredValues = this.title === 'Update Customer' || this.title === 'Add Customer';
  }

  /**
   * runs on change of input variable customer
   * updates customerModel linked to form with customer input
   * @param changes list of changes to component
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['customer']) {
      if (this.customer !== undefined) {
        this.customerModel = Object.assign({}, this.customer);
      }
    }
  }
}
