import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Page1Component} from './page1.component';

import {of} from 'rxjs';
import {AppService} from "../../app.service";
import {HttpClientModule} from "@angular/common/http";

class FakeAppService extends AppService {
  getPage1() {
    return of({
      content: 'Test content'
    });
  }
}

describe('Page1Component', () => {
  let component: Page1Component;
  let fixture: ComponentFixture<Page1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Page1Component
      ],
      imports: [
        HttpClientModule
      ]
    }).overrideComponent(Page1Component, {
      set: {
        providers: [
          {provide: AppService, useClass: FakeAppService}
        ]
      }
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Page1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
