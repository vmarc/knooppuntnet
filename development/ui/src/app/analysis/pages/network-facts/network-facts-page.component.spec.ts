import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkFactsPageComponent } from './network-facts-page.component';

describe('NetworkFactsPageComponent', () => {
  let component: NetworkFactsPageComponent;
  let fixture: ComponentFixture<NetworkFactsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkFactsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkFactsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
