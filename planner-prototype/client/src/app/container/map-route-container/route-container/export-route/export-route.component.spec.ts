import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExportRouteComponent } from './export-route.component';

describe('ExportRouteComponent', () => {
  let component: ExportRouteComponent;
  let fixture: ComponentFixture<ExportRouteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExportRouteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExportRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
