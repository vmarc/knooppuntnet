import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetNetworkListComponent } from './subset-network-list.component';

describe('SubsetNetworkListComponent', () => {
  let component: SubsetNetworkListComponent;
  let fixture: ComponentFixture<SubsetNetworkListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetNetworkListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetNetworkListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
