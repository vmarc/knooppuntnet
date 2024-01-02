import { ChangeDetectionStrategy, inject } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { UserService } from '../user';

@Component({
  selector: 'kpn-base-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-sidebar>
      <div id="extraFunctions" (click)="toggleExtraFunctions()" class="extra-functions"></div>

      @if (extraFunctionsEnabled) {
        <ul>
          <li>
            <p>
              <a routerLink="/poi/areas">Point of interest areas</a>
            </p>
          </li>
          <li>
            <p>
              <a routerLink="/status">Status</a>
            </p>
          </li>
          <li>
            <p>
              <a routerLink="/symbols">Symbols</a>
            </p>
          </li>
          <li>
            <button mat-stroked-button (click)="forceError()">Sentry, force error</button>
          </li>
          <li>
            <button mat-stroked-button (click)="users()">Users</button>
          </li>
        </ul>
      }
    </kpn-sidebar>
  `,
  styles: `
    .extra-functions {
      float: right;
      width: 20px;
      height: 20px;
    }
  `,
  standalone: true,
  imports: [SidebarComponent, RouterLink, MatButtonModule],
})
export class BaseSidebarComponent {
  extraFunctionsEnabled = false;
  private readonly userService = inject(UserService);

  toggleExtraFunctions(): void {
    this.extraFunctionsEnabled = !this.extraFunctionsEnabled;
  }

  forceError(): void {
    throw new Error('Forced error to verify Sentry reporting');
  }

  users(): void {
    this.userService.users();
  }
}
