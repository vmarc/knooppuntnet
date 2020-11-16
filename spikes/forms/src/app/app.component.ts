import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="page">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .page {
      margin: 1em;
    }
  `]
})
export class AppComponent {
}
