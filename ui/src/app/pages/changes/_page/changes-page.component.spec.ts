import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangesPageComponent } from './changes-page.component';

describe('ChangesPageComponent', () => {
  let component: ChangesPageComponent;
  let fixture: ComponentFixture<ChangesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangesPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
