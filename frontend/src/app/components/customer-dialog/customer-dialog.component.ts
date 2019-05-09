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
  styleUrls: ['./customer-dialog.component.scss']
})
export class CustomerDialogComponent implements OnInit, OnChanges {

  @Input() title: string;
  @Input() customer: Customer;
  @Output() submitCustomer = new EventEmitter<Customer>();
  private customerModel: Customer = new Customer(null, null, null, null, null);
  private requiredValues: boolean;

  constructor() {
  }

  onSubmit() {
    this.submitCustomer.emit(this.customerModel);
  }

  ngOnInit() {
    this.requiredValues = this.title === 'Update Customer' || this.title === 'Add Customer';
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['customer']) {
      if (this.customer !== undefined) {
        this.customerModel = Object.assign({}, this.customer);
      }
    }
  }
}
