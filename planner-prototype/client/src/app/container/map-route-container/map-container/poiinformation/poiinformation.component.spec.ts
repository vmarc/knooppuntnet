import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PoiinformationComponent } from './poiinformation.component';

describe('PoiinformationComponent', () => {
  let component: PoiinformationComponent;
  let fixture: ComponentFixture<PoiinformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoiinformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoiinformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
