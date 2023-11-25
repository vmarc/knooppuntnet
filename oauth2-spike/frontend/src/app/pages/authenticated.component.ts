import { Component } from '@angular/core';
import { UserService } from "../service/user.service";

@Component({
  selector: 'app-authenticated',
  standalone: true,
  template: `
    <p>authenticated</p>
  `,
})
export class AuthenticatedComponent {
  constructor(private userService: UserService) {
    userService.authenticated();
  }
}
