import { Component } from '@angular/core';
import { PageComponent } from './page.component';

@Component({
  selector: 'kpn-page-3',
  standalone: true,
  template: `
    <kpn-page title="Page3">
      <p>page three</p>
    </kpn-page>
  `,
  imports: [PageComponent],
})
export class Page3Component {}
