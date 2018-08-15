import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetNetworkComponent } from './subset-network.component';

describe('SubsetNetworkComponent', () => {
  let component: SubsetNetworkComponent;
  let fixture: ComponentFixture<SubsetNetworkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetNetworkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetNetworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
