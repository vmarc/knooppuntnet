import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {SubsetSidenavComponent} from './subset-sidenav.component';

describe('SubsetSidenavComponent', () => {
  let component: SubsetSidenavComponent;
  let fixture: ComponentFixture<SubsetSidenavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetSidenavComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetSidenavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
