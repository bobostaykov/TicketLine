import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StornoComponent } from './storno.component';

describe('StornoComponent', () => {
  let component: StornoComponent;
  let fixture: ComponentFixture<StornoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StornoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StornoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
