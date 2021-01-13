import {Component} from '@angular/core';

@Component({
  selector: 'app-menu',
  template: `
    <a routerLink="/page1">page 1</a> |
    <a routerLink="/page2">page 2</a> |
    <a routerLink="/page3">page 3</a> |
    <a routerLink="/page4">page 4</a> |
    <a routerLink="/page5">page 5</a> |
    <a routerLink="/page6">page 6</a>
  `
})
export class MenuComponent {
}
