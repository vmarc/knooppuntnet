import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {SubsetOrphanNodesPageComponent} from './subset-orphan-nodes-page.component';

describe('SubsetOrphanNodesPageComponent', () => {
  let component: SubsetOrphanNodesPageComponent;
  let fixture: ComponentFixture<SubsetOrphanNodesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetOrphanNodesPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsetOrphanNodesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
