import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {NetworkNodesPageComponent} from './network-nodes-page.component';

describe('NetworkNodesPageComponent', () => {
  let component: NetworkNodesPageComponent;
  let fixture: ComponentFixture<NetworkNodesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NetworkNodesPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkNodesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
