import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {SubsetOrphanRoutesPageComponent} from './subset-orphan-routes-page.component';

describe('SubsetOrphanRoutesPageComponent', () => {
  let component: SubsetOrphanRoutesPageComponent;
  let fixture: ComponentFixture<SubsetOrphanRoutesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetOrphanRoutesPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetOrphanRoutesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
