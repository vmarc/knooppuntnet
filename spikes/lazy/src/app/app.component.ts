import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app">
      <div>
        Lazy loading proof of concept
      </div>
      <p>
        <a [routerLink]="'/'">Home</a> |
        <a [routerLink]="'/main-feature'">Main feature</a>
      </p>
      <div class="router-outlet">
        <router-outlet></router-outlet>
      </div>
    </div>
  `
})
export class AppComponent {
}
