import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetNetworkTableComponent } from './subset-network-table.component';

describe('SubsetNetworkTableComponent', () => {
  let component: SubsetNetworkTableComponent;
  let fixture: ComponentFixture<SubsetNetworkTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetNetworkTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetNetworkTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
