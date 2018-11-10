import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapDetailNodeComponent } from './map-detail-node.component';

describe('MapDetailNodeComponent', () => {
  let component: MapDetailNodeComponent;
  let fixture: ComponentFixture<MapDetailNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapDetailNodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapDetailNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
