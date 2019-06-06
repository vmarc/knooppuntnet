import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapRouteContainerComponent } from './map-route-container.component';

describe('MapRouteContainerComponent', () => {
  let component: MapRouteContainerComponent;
  let fixture: ComponentFixture<MapRouteContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapRouteContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapRouteContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
