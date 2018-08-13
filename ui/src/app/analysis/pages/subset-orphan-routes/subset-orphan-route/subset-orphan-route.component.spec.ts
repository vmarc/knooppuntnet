import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetOrphanRouteComponent } from './subset-orphan-route.component';

describe('SubsetOrphanRouteComponent', () => {
  let component: SubsetOrphanRouteComponent;
  let fixture: ComponentFixture<SubsetOrphanRouteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetOrphanRouteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetOrphanRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
