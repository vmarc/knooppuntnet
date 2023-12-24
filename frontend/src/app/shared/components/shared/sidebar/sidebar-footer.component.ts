import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Router } from '@angular/router';
import { VersionService } from '@app/services';
import { UserLinkLogoutComponent } from '../../../user';
import { UserLinkLoginComponent } from '../../../user';
import { UserStore } from '../../../user/user.store';

@Component({
  selector: 'kpn-sidebar-footer',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="footer">
      <ul class="links">
        <!-- eslint-disable @angular-eslint/template/i18n -->
        <li><a [href]="link('en')">English</a></li>
        <li><a [href]="link('nl')">Nederlands</a></li>
        <li><a [href]="link('fr')">Français</a></li>
        <li><a [href]="link('de')">Deutsch</a></li>
        <!-- eslint-enable @angular-eslint/template/i18n -->
      </ul>

      <p class="version">
        {{ version() }}
      </p>

      @if (loginEnabled) {
        @if (loggedIn()) {
          <p>
            {{ user() }}
            <br />
            <kpn-user-link-logout />
          </p>
        } @else {
          <p>
            <kpn-user-link-login />
          </p>
        }
      }
    </div>
  `,
  styles: `
    .footer {
      padding-top: 15px;
      border-top-width: 1px;
      border-top-style: solid;
      border-top-color: lightgray;
      text-align: center;
    }

    .version {
      color: lightgray;
    }
  `,
  standalone: true,
  imports: [UserLinkLoginComponent, UserLinkLogoutComponent],
})
export class SidebarFooterComponent {
  private readonly userStore = inject(UserStore);
  readonly loggedIn = this.userStore.loggedIn;
  readonly user = this.userStore.user;

  @Input({ required: false }) loginEnabled = true;

  constructor(
    private router: Router,
    private versionService: VersionService
  ) {}

  version(): string {
    return this.versionService.version;
  }

  link(language: string): string {
    let path = this.router.url;
    if (
      path.startsWith('/en/') ||
      path.startsWith('/nl/') ||
      path.startsWith('/fr/') ||
      path.startsWith('/de/')
    ) {
      path = path.substring(3);
    }
    return `/${language}${path}`;
  }
}
