import {Component} from '@angular/core';

@Component({
  selector: 'app-home',
  template: `
    <a routerLink="../counter">counter</a> |
    <a routerLink="../node">node</a> |
    <a routerLink="../issues">issues</a>
  `
})
export class HomeComponent {
}
