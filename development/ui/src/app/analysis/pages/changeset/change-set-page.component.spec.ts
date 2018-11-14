import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {ChangeSetPageComponent} from './change-set-page.component';

describe('ChangeSetPageComponent', () => {
  let component: ChangeSetPageComponent;
  let fixture: ComponentFixture<ChangeSetPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ChangeSetPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeSetPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
