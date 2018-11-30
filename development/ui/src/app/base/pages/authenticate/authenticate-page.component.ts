import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../user.service";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-authenticate-page',
  template: `
    <h1>
      Authenticate
    </h1>
  `
})
export class AuthenticatePageComponent implements OnInit {

  constructor(private userService: UserService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.userService.authenticated();
  }

}
