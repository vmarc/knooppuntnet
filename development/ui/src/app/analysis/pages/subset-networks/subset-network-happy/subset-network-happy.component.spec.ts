import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsetNetworkHappyComponent } from './subset-network-happy.component';

describe('SubsetNetworkHappyComponent', () => {
  let component: SubsetNetworkHappyComponent;
  let fixture: ComponentFixture<SubsetNetworkHappyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubsetNetworkHappyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetNetworkHappyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
