import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <div sidenav>
        <router-outlet name="sidebar"></router-outlet>
      </div>
      <div content>
        <router-outlet></router-outlet>
      </div>
    </kpn-page>
  `
})
export class AppComponent {

}
