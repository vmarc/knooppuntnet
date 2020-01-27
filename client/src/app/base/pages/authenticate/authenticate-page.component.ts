import {Component, OnInit} from "@angular/core";
import {PageService} from "../../../components/shared/page.service";
import {UserService} from "../../../services/user.service";

@Component({
  selector: "kpn-authenticate-page",
  template: `
    <h1 i18n="@@authenticate-page.title">
      Authenticate
    </h1>
  `
})
export class AuthenticatePageComponent implements OnInit {

  constructor(private userService: UserService,
              private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.userService.authenticated();
  }

}
