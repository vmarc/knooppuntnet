import {Component} from '@angular/core';

@Component({
  selector: 'kpn-sidenav',
  template: `
    <mat-nav-list>
      <a mat-list-item routerLink="/about">about</a>
      <a mat-list-item routerLink="/overview" [ngClass]="{'nav-item-selected': true}">overview</a>
      <a mat-list-item routerLink="/login">login</a>
      <a mat-list-item routerLink="/logout">logout</a>
    </mat-nav-list>
  `
})
export class SidenavComponent {

}
