import {Component, OnInit} from "@angular/core";
import {PageService} from "../../../components/shared/page.service";
import {UserService} from "../../../services/user.service";

@Component({
  selector: "kpn-authenticate-page",
  template: `
    <span i18n="@@authenticate-page.title">
      Logging in...
    </span>
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
