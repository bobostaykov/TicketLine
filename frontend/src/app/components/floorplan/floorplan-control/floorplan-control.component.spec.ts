import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FloorplanControlComponent } from './floorplan-control.component';

describe('FloorplanControlComponent', () => {
  let component: FloorplanControlComponent;
  let fixture: ComponentFixture<FloorplanControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FloorplanControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FloorplanControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
