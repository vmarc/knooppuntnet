import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SidenavNetworkComponent } from './sidenav-network.component';

describe('SidenavNetworkComponent', () => {
  let component: SidenavNetworkComponent;
  let fixture: ComponentFixture<SidenavNetworkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SidenavNetworkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavNetworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
