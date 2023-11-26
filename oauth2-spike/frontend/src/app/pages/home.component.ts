import { Component } from '@angular/core';
import { PageComponent } from "./page.component";

@Component({
  selector: 'kpn-home',
  standalone: true,
  template: `
    <kpn-page title="Home">
      <p>home</p>
    </kpn-page>
  `,
  imports: [
    PageComponent
  ]
})
export class HomeComponent {
}
