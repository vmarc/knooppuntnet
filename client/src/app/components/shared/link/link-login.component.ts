import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'kpn-link-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a routerLink="/login" (click)="registerLoginCallbackPage()" i18n="@@login"
      >login</a
    >
  `,
})
export class LinkLoginComponent {
  constructor(private userService: UserService) {}

  registerLoginCallbackPage() {
    this.userService.registerLoginCallbackPage();
  }
}
