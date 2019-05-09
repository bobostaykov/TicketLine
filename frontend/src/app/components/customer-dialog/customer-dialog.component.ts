import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Customer} from '../../dtos/customer';

@Component({
  selector: 'app-customer-dialog',
  templateUrl: './customer-dialog.component.html',
  styleUrls: ['./customer-dialog.component.scss']
})
export class CustomerDialogComponent implements OnInit {

  @Input() title: string;
  @Input() customer?: Customer;
  @Output() submitCustomer = new EventEmitter<Customer>();
  constructor() { }

  ngOnInit() {
    if (this.customer === undefined) {
      this.customer = new Customer(null, null, null, null, null);
    }
  }
  onSubmit() {
    this.submitCustomer.emit(this.customer);
  }
}
