import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-link-logout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a routerLink="/logout" i18n="@@link.logout">logout</a>
  `
})
export class LinkLogoutComponent {
}
