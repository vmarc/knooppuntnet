import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidebar-sub-item',
  template: `
    <a mat-list-item routerLink="{{link}}"><span><ng-content></ng-content></span></a>
  `,
  styles: [`
    span {
      margin-left: 30px;
    }
  `]
})
export class SidebarSubItemComponent {

  @Input() link: string;

}
