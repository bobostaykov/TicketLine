import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FloorplanSvgComponent } from './floorplan-svg.component';

describe('FloorplanSvgComponent', () => {
  let component: FloorplanSvgComponent;
  let fixture: ComponentFixture<FloorplanSvgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FloorplanSvgComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FloorplanSvgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
