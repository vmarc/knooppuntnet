import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../user.service";

@Component({
  selector: 'kpn-authenticate-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Authenticate
        </h1>
      </div>
    </kpn-page>
  `
})
export class AuthenticatePageComponent implements OnInit {

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userService.authenticated();
  }

}
