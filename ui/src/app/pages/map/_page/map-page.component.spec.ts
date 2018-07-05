import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapPageComponent } from './map-page.component';

describe('MapPageComponent', () => {
  let component: MapPageComponent;
  let fixture: ComponentFixture<MapPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
