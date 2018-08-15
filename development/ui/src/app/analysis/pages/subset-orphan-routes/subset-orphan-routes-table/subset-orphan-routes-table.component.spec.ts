import {ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
import {SubsetOrphanRoutesTableComponent} from './subset-orphan-routes-table.component';

describe('SubsetOrphanRoutesTableComponent', () => {
  let component: SubsetOrphanRoutesTableComponent;
  let fixture: ComponentFixture<SubsetOrphanRoutesTableComponent>;

  beforeEach(fakeAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SubsetOrphanRoutesTableComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SubsetOrphanRoutesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
