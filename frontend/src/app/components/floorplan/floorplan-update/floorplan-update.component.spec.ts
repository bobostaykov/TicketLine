import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FloorplanUpdateComponent } from './floorplan-update.component';

describe('FloorplanUpdateComponent', () => {
  let component: FloorplanUpdateComponent;
  let fixture: ComponentFixture<FloorplanUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FloorplanUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FloorplanUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
