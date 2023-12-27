import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from './user.service';

@Component({
  selector: 'kpn-user-link-logout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a rel="nofollow noreferrer" (click)="logout()">logout</a> `,
  standalone: true,
})
export class UserLinkLogoutComponent {
  private readonly userService = inject(UserService);

  logout(): void {
    this.userService.logout();
  }
}
