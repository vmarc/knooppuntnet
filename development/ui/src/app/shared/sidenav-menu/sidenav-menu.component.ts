import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidenav-menu',
  templateUrl: './sidenav-menu.component.html',
  styleUrls: ['./sidenav-menu.component.scss']
})
export class SidenavMenuComponent {

  @Input() open: boolean = true;
  @Input() title: string;

  toggleOpen(): void {
    this.open = !this.open;
  }

}
