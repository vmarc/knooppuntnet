import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from './user.service';

@Component({
  selector: 'kpn-user-link-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<a rel="nofollow noreferrer" (click)="click()">login</a>`,
  standalone: true,
})
export class UserLinkLoginComponent {
  private readonly userService = inject(UserService);

  click(): void {
    this.userService.login();
  }
}
