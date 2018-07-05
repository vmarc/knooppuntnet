import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetChangesPageComponent } from './subset-changes-page.component';

describe('SubsetChangesPageComponent', () => {
  let component: SubsetChangesPageComponent;
  let fixture: ComponentFixture<SubsetChangesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetChangesPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetChangesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
