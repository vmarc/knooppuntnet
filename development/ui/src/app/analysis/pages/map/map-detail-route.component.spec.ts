import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {MapDetailRouteComponent} from './map-detail-route.component';

describe('MapDetailRouteComponent', () => {
  let component: MapDetailRouteComponent;
  let fixture: ComponentFixture<MapDetailRouteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MapDetailRouteComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapDetailRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
