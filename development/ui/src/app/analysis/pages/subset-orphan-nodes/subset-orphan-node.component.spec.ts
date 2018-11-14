import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {SubsetOrphanNodeComponent} from './subset-orphan-node.component';

describe('SubsetOrphanNodeComponent', () => {
  let component: SubsetOrphanNodeComponent;
  let fixture: ComponentFixture<SubsetOrphanNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetOrphanNodeComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetOrphanNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
