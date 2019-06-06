import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RouteContainerComponent } from './route-container.component';

describe('RouteContainerComponent', () => {
  let component: RouteContainerComponent;
  let fixture: ComponentFixture<RouteContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RouteContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RouteContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
