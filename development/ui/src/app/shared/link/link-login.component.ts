import {Component} from "@angular/core";
import {UserService} from "../../user.service";

@Component({
  selector: 'kpn-link-login',
  template: `
    <a routerLink="/login" (click)="registerLoginCallbackPage()">Login</a>
  `
})
export class LinkLoginComponent {

  constructor(private userService: UserService) {
  }

  registerLoginCallbackPage() {
    this.userService.registerLoginCallbackPage();
  }

}
