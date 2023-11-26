import { Component } from '@angular/core';
import { PageComponent } from './page.component';

@Component({
  selector: 'kpn-page-1',
  standalone: true,
  template: `
    <kpn-page title="Page1">
      <p>page one</p>
    </kpn-page>
  `,
  imports: [PageComponent],
})
export class Page1Component {}
