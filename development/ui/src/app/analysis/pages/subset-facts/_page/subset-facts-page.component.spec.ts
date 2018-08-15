import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetFactsPageComponent } from './subset-facts-page.component';

describe('SubsetFactsPageComponent', () => {
  let component: SubsetFactsPageComponent;
  let fixture: ComponentFixture<SubsetFactsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetFactsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetFactsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
