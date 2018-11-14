import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {NetworkRoutesPageComponent} from './network-routes-page.component';

describe('NetworkRoutesPageComponent', () => {
  let component: NetworkRoutesPageComponent;
  let fixture: ComponentFixture<NetworkRoutesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NetworkRoutesPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkRoutesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
