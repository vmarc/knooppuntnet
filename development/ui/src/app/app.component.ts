import {Component} from '@angular/core';
import {IconService} from "./icon.service";

@Component({
  selector: 'app-root',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <div sidebar>
        <router-outlet name="sidebar"></router-outlet>
      </div>
      <div content>
        <router-outlet></router-outlet>
      </div>
    </kpn-page>
  `
})
export class AppComponent {

  constructor(private iconService: IconService) {
  }

}
