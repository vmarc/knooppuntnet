import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {MapDetailDefaultComponent} from './map-detail-default.component';

describe('MapDetailDefaultComponent', () => {
  let component: MapDetailDefaultComponent;
  let fixture: ComponentFixture<MapDetailDefaultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MapDetailDefaultComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapDetailDefaultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
