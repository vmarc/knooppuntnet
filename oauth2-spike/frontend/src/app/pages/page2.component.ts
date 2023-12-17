import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from './page.component';
import { Page2Store } from './page2.store';

@Component({
  selector: 'kpn-page-2',
  standalone: true,
  template: `
    <kpn-page title="Page2">
      <p>page two</p>
      @if (page(); as page) {
        {{ page }}
      }
    </kpn-page>
  `,
  imports: [PageComponent],
  providers: [Page2Store],
})
export class Page2Component {
  private readonly store = inject(Page2Store);
  readonly page = this.store.page;
}
