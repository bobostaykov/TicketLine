import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationAndCancellationComponent } from './reservation-and-cancellation.component';

describe('ReservationAndCancellationComponent', () => {
  let component: ReservationAndCancellationComponent;
  let fixture: ComponentFixture<ReservationAndCancellationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReservationAndCancellationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationAndCancellationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
