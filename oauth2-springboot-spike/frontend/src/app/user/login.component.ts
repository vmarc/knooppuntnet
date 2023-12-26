import { Component } from '@angular/core';
import { PageComponent } from "../pages/page.component";

@Component({
  selector: 'kpn-login',
  standalone: true,
  template: `
    <kpn-page title="Home">
      <p>Logged in</p>
    </kpn-page>    
  `,
  imports: [
    PageComponent
  ]
})
export class LoginComponent {
}
