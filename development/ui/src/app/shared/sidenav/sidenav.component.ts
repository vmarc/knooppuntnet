import {Component} from '@angular/core';
import {UserService} from "../../user.service";

@Component({
  selector: 'kpn-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent {

  constructor(private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

}
