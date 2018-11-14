import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {SubsetNetworksPageComponent} from './subset-networks-page.component';

describe('SubsetNetworksPageComponent', () => {
  let component: SubsetNetworksPageComponent;
  let fixture: ComponentFixture<SubsetNetworksPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetNetworksPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetNetworksPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
