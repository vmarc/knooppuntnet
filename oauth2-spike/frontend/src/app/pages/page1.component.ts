import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from './page.component';
import { Page1Store } from './page1.store';

@Component({
  selector: 'kpn-page-1',
  template: `
    <kpn-page title="Page1">
      <p>page one</p>
      @if (page(); as page) {
        {{ page }}
      }
    </kpn-page>
  `,
  providers: [Page1Store],
  standalone: true,
  imports: [PageComponent],
})
export class Page1Component implements OnInit, OnDestroy {
  private readonly store = inject(Page1Store);
  readonly page = this.store.page;

  constructor() {
    console.log(`Page1Component.constructor()`);
  }

  ngOnInit(): void {
    console.log(`Page1Component.ngOnInit()`);
  }

  ngOnDestroy(): void {
    console.log(`Page1Component.ngOnDestroy()`);
  }
}
