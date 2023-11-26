import { Component } from '@angular/core';
import { UserService } from "../service/user.service";

@Component({
  selector: 'kpn-authenticated',
  standalone: true,
  template: `
    <p>logging in...</p>
  `,
})
export class AuthenticatedComponent {
  constructor(private userService: UserService) {
    userService.authenticated();
  }
}
