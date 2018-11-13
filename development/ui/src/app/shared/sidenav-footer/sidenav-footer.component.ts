import {Component} from '@angular/core';
import {UserService} from "../../user.service";

@Component({
  selector: 'kpn-sidenav-footer',
  templateUrl: './sidenav-footer.component.html',
  styleUrls: ['./sidenav-footer.component.scss']
})
export class SidenavFooterComponent {

  constructor(private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

  isLoggedIn(): boolean {
    return this.currentUser().length > 0;
  }

}
