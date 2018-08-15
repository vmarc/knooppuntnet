
import { fakeAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangesTableComponent } from './changes-table.component';

describe('ChangesTableComponent', () => {
  let component: ChangesTableComponent;
  let fixture: ComponentFixture<ChangesTableComponent>;

  beforeEach(fakeAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangesTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
