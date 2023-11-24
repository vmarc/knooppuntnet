import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  template: `
    <p>oauth2 spike</p>
    <router-outlet></router-outlet>
  `,
  styles: [],
})
export class AppComponent {
}
