import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'kpn-link-logout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a rel="nofollow noreferrer" (click)="logout()">logout</a> `,
  standalone: true,
})
export class LinkLogoutComponent {
  private readonly userService = inject(UserService);

  logout(): void {
    this.userService.logoutLinkClicked();
  }
}
