import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkChangesPageComponent } from './network-changes-page.component';

describe('NetworkChangesPageComponent', () => {
  let component: NetworkChangesPageComponent;
  let fixture: ComponentFixture<NetworkChangesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkChangesPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkChangesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
