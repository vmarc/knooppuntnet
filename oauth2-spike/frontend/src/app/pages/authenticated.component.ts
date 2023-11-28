import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'kpn-authenticated',
  standalone: true,
  template: ` <p>logging in...</p> `,
})
export class AuthenticatedComponent {
  private readonly userService = inject(UserService);

  constructor() {
    this.userService.authenticated();
  }
}
