import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkMapPageComponent } from './network-map-page.component';

describe('NetworkMapPageComponent', () => {
  let component: NetworkMapPageComponent;
  let fixture: ComponentFixture<NetworkMapPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkMapPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkMapPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
