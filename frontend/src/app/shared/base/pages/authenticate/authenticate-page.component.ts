import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../../../user';
import { UserErrorComponent } from '../../../user/user-error.component';

@Component({
  selector: 'kpn-authenticate-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@authenticate-page.title">Logging in...</p>
    <kpn-user-error />
  `,
  standalone: true,
  imports: [UserErrorComponent],
})
export class AuthenticatePageComponent {
  private readonly userService = inject(UserService);

  constructor() {
    this.userService.authenticated();
  }
}
