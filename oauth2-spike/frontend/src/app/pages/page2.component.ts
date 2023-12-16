import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from './page.component';
import { Page2Service } from './page2.service';

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
  providers: [Page2Service],
})
export class Page2Component {
  private readonly service = inject(Page2Service);
  readonly page = this.service.page;
}
