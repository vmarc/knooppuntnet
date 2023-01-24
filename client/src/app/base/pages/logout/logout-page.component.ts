import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionUserLogout } from '../../../core/user/user.actions';
import { selectUserLoggedIn } from '../../../core/user/user.selectors';

@Component({
  selector: 'kpn-logout-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header subject="logout-page" i18n="@@logout.title"
      >Logout
    </kpn-page-header>

    <div *ngIf="loggedIn$ | async; else notLoggedIn">
      <div class="note-page-contents">
        <p i18n="@@logout.logged-in">
          You are currently logged in as
          <kpn-osm-link-user></kpn-osm-link-user>
          .
        </p>

        <p i18n="@@logout.note.1">
          This allows you to view extra information that is available to
          OpenStreetMap users only.
        </p>

        <p i18n="@@logout.note.2">
          After logging out you can continue to use this website. However, the
          additional information such as changesets, network changes, route
          changes and node changes will not be visible anymore. Your personal
          OpenStreetMap
          <kpn-osm-link-user-oath-clients></kpn-osm-link-user-oath-clients>
          will still contain this application. The application can safely be
          revoked. The authorization will not be used anymore. A new
          authorization will be created when logging in again.
        </p>
      </div>

      <button
        mat-raised-button
        color="primary"
        (click)="logout()"
        i18n="@@logout.submit"
      >
        Logout
      </button>
    </div>

    <ng-template #notLoggedIn>
      <div>
        <p i18n="@@logout.not-logged-in">You are not logged in.</p>
        <p i18n="@@logout.login-comment">
          You can <a routerLink="/login">login</a> to view extra information
          that is available to
          <kpn-osm-website></kpn-osm-website>
          users only.
        </p>
      </div>
    </ng-template>
  `,
})
export class LogoutPageComponent {
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);

  constructor(private store: Store) {}

  logout(): void {
    this.store.dispatch(actionUserLogout());
  }
}
