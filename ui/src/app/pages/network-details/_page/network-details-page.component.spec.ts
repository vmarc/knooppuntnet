import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkDetailsPageComponent } from './network-details-page.component';

describe('NetworkDetailsPageComponent', () => {
  let component: NetworkDetailsPageComponent;
  let fixture: ComponentFixture<NetworkDetailsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkDetailsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
