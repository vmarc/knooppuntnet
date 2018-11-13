import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkSidenavComponent } from './network-sidenav.component';

describe('NetworkSidenavComponent', () => {
  let component: NetworkSidenavComponent;
  let fixture: ComponentFixture<NetworkSidenavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkSidenavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkSidenavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
