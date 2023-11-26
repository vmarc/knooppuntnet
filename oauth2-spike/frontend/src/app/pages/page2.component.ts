import { Component } from '@angular/core';
import { PageComponent } from './page.component';

@Component({
  selector: 'kpn-page-2',
  standalone: true,
  template: `
    <kpn-page title="Page2">
      <p>page two</p>
    </kpn-page>
  `,
  imports: [PageComponent],
})
export class Page2Component {}
