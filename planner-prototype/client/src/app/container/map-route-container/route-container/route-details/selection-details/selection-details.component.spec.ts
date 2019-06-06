import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectionDetailsComponent } from './selection-details.component';

describe('SelectionDetailsComponent', () => {
  let component: SelectionDetailsComponent;
  let fixture: ComponentFixture<SelectionDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectionDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
