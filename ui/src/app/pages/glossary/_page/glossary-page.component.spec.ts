import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GlossaryPageComponent } from './glossary-page.component';

describe('GlossaryPageComponent', () => {
  let component: GlossaryPageComponent;
  let fixture: ComponentFixture<GlossaryPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GlossaryPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GlossaryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
