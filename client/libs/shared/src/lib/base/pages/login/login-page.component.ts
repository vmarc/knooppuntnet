import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { actionUserLogin } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-login-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-page-header subject="login-page" i18n="@@login.title"
        >Login
      </kpn-page-header>

      <div class="note-page-contents">
        <p i18n="@@login.note.1">
          Login allows you to view extra information, such as changeset details,
          network history, route history and node history.
        </p>
        <p i18n="@@login.note.2">
          This information is available to registered OpenStreetMap users only.
        </p>
        <p i18n="@@login.note.3">
          After clicking the "login" button below, you will be directed (via the
          OpenStreetMap login page if you are not logged in to OpenStreetMap
          yet) to the OpenStreetMap page "Authorize access to your account".
          Click the button "Grant access" (leave the option "read your user
          preferences" checked).
        </p>
        <p i18n="@@login.note.4">
          The knooppuntnet application only reads your username from the user
          preferences to establish that you are a registered OpenStreetMap user.
          All other user preferences are ignored, and not stored in
          knooppuntnet.
        </p>
        <p i18n="@@login.note.5">
          This login procedure uses OpenStreetMap security. Knooppuntnet does
          not have access to your password at any moment.
        </p>
        <p i18n="@@login.note.6">
          You remain logged in (also after closing you browser), until you
          explicitly logout through the logout page. To login again after logout
          you will have to use the same login procedure again.
        </p>
      </div>

      <button
        mat-raised-button
        color="primary"
        (click)="login()"
        i18n="@@login.submit"
      >
        Login
      </button>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    PageComponent,
    PageHeaderComponent,
    SidebarComponent,
  ],
})
export class LoginPageComponent {
  constructor(private store: Store) {}

  login() {
    this.store.dispatch(actionUserLogin());
  }
}
