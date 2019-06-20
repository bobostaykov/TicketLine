import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TicketCheckReservationComponent} from './ticket-check.component';

describe('TicketCheckReservationComponent', () => {
  let component: TicketCheckReservationComponent;
  let fixture: ComponentFixture<TicketCheckReservationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TicketCheckReservationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketCheckReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
