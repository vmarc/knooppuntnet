import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../service/user.service';
import { UserStore } from '../service/user.store';

@Component({
  selector: 'kpn-authenticated',
  standalone: true,
  template: `
    <p>logging in...</p>
    @if (error(); as message) {
      <p class="error">
        {{ message }}
      </p>
    }
    @if (errorDetail(); as message) {
      <p class="error">
        {{ message }}
      </p>
    }
  `,
})
export class AuthenticatedComponent {
  private readonly userService = inject(UserService);
  private readonly userStore = inject(UserStore);
  readonly error = this.userStore.error;
  readonly errorDetail = this.userStore.errorDetail;

  constructor() {
    this.userService.authenticated();
  }
}
