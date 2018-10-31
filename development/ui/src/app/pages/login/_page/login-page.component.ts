import {Component} from '@angular/core';
import {UserService} from "../../../user.service";

@Component({
  selector: 'kpn-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {

  constructor(private userService: UserService) {
  }

  login() {
    this.userService.login();
  }

}
