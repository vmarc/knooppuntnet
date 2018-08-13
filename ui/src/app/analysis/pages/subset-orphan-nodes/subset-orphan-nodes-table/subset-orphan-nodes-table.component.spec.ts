import {ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
import {SubsetOrphanNodesTableComponent} from './subset-orphan-nodes-table.component';

describe('SubsetOrphanNodesTableComponent', () => {
  let component: SubsetOrphanNodesTableComponent;
  let fixture: ComponentFixture<SubsetOrphanNodesTableComponent>;

  beforeEach(fakeAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetOrphanNodesTableComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SubsetOrphanNodesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
