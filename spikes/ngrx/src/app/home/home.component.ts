import {Component} from '@angular/core';

@Component({
  selector: 'app-home',
  template: `
    <a routerLink="../counter">counter</a> |
    <a routerLink="../node">node</a> |
    <a routerLink="../issues">issues</a> |
    <a routerLink="../users">users</a>
  `
})
export class HomeComponent {
}
