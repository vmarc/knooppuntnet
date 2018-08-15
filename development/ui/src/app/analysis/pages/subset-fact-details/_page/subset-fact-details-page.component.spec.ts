import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetFactDetailsPageComponent } from './subset-fact-details-page.component';

describe('SubsetFactDetailsPageComponent', () => {
  let component: SubsetFactDetailsPageComponent;
  let fixture: ComponentFixture<SubsetFactDetailsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetFactDetailsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetFactDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
