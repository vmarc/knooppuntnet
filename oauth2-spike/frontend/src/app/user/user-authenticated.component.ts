import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserErrorComponent } from './user-error.component';
import { UserService } from './user.service';

@Component({
  selector: 'kpn-user-authenticated',
  standalone: true,
  template: `
    <p>logging in...</p>
    <kpn-user-error />
  `,
  imports: [UserErrorComponent],
})
export class UserAuthenticatedComponent {
  private readonly userService = inject(UserService);

  constructor() {
    this.userService.authenticated();
  }
}
