import { Component } from '@angular/core';
import { UserService } from "../service/user.service";

@Component({
  selector: 'app-login',
  standalone: true,
  template: `
    <p>login</p>
    <button (click)="login()">login</button>
  `,
})
export class LoginComponent {
  constructor(private userService: UserService) {
  }

  login(): void {
    this.userService.login();
  }
}
