import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FloorplanTicketComponent } from './floorplan-ticket.component';

describe('FloorplanTicketComponent', () => {
  let component: FloorplanTicketComponent;
  let fixture: ComponentFixture<FloorplanTicketComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FloorplanTicketComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FloorplanTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
