import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SidenavSubsetsComponent } from './sidenav-subsets.component';

describe('SidenavSubsetsComponent', () => {
  let component: SidenavSubsetsComponent;
  let fixture: ComponentFixture<SidenavSubsetsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SidenavSubsetsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavSubsetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
