import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LinksPageComponent } from './links-page.component';

describe('LinksPageComponent', () => {
  let component: LinksPageComponent;
  let fixture: ComponentFixture<LinksPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LinksPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinksPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
