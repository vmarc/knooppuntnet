import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SidenavSubsetComponent } from './sidenav-subset.component';

describe('SidenavSubsetComponent', () => {
  let component: SidenavSubsetComponent;
  let fixture: ComponentFixture<SidenavSubsetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SidenavSubsetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavSubsetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
