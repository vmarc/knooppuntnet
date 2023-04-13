import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Router } from '@angular/router';
import { selectUserUser } from '@app/core/user';
import { selectUserLoggedIn } from '@app/core/user';
import { VersionService } from '@app/services';
import { Store } from '@ngrx/store';

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

      <ng-container *ngIf="loginEnabled">
        <p *ngIf="loggedIn$ | async; else notLoggedIn">
          {{ user$ | async }}
          <br />
          <kpn-link-logout />
        </p>
        <ng-template #notLoggedIn>
          <p>
            <kpn-link-login />
          </p>
        </ng-template>
      </ng-container>
    </div>
  `,
  styles: [
    `
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
  ],
})
export class SidebarFooterComponent {
  @Input() loginEnabled = true;
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);
  readonly user$ = this.store.select(selectUserUser);

  constructor(
    private router: Router,
    private versionService: VersionService,
    private store: Store
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
