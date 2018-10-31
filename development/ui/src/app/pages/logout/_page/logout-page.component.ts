import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../user.service";

@Component({
  selector: 'kpn-logout-page',
  templateUrl: './logout-page.component.html',
  styleUrls: ['./logout-page.component.scss']
})
export class LogoutPageComponent {

  constructor(private userService: UserService) {
  }

  logout() {
    this.userService.logout();
  }

  loggedIn() {
    return this.loggedInUser() != "";
  }

  loggedInUser() {
    return this.userService.currentUser();
  }

}
