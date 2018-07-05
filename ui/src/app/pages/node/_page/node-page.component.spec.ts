import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NodePageComponent } from './node-page.component';

describe('NodePageComponent', () => {
  let component: NodePageComponent;
  let fixture: ComponentFixture<NodePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NodePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NodePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
