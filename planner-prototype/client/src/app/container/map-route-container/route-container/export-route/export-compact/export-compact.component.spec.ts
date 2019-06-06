import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExportCompactComponent } from './export-compact.component';

describe('ExportCompactComponent', () => {
  let component: ExportCompactComponent;
  let fixture: ComponentFixture<ExportCompactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExportCompactComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExportCompactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
