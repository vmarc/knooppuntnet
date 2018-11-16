import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidenav-sub-item',
  template: `
    <a mat-list-item routerLink="{{link}}"><span><ng-content></ng-content></span></a>
  `,
  styles: [`
    span {
      margin-left: 30px;
    }
  `]
})
export class SidenavSubItemComponent {

  @Input() link: string;

}
