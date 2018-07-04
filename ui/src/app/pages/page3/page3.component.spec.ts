import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Page3Component} from './page3.component';
import {HttpClientModule} from "@angular/common/http";
import {AppService} from "../../app.service";
import {of} from "rxjs/index";

class FakeAppService extends AppService {
  getPage3() {
    return of({
      content: 'Test content'
    });
  }
}

describe('Page3Component', () => {
  let component: Page3Component;
  let fixture: ComponentFixture<Page3Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Page3Component
      ],
      imports: [
        HttpClientModule
      ]
    }).overrideComponent(Page3Component, {
      set: {
        providers: [
          {provide: AppService, useClass: FakeAppService}
        ]
      }
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Page3Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
