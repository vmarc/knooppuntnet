import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'kpn-link-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a rel="nofollow noreferrer" (click)="login()">login</a> `,
  standalone: true,
})
export class LinkLoginComponent {
  constructor(private userService: UserService) {}

  login(): void {
    this.userService.loginLinkClicked();
  }
}
