import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Page2Component} from './page2.component';
import {HttpClientModule} from "@angular/common/http";
import {AppService} from "../../app.service";
import {of} from "rxjs/index";

class FakeAppService extends AppService {
  getPage2() {
    return of({
      content: 'Test content'
    });
  }
}

describe('Page2Component', () => {
  let component: Page2Component;
  let fixture: ComponentFixture<Page2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Page2Component
      ],
      imports: [
        HttpClientModule
      ]
    }).overrideComponent(Page2Component, {
      set: {
        providers: [
          {provide: AppService, useClass: FakeAppService}
        ]
      }
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Page2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
