import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkChangesComponent } from './components/network-changes.component';
import { NetworkNotFoundComponent } from '../components/network-not-found.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkChangesPageService } from './network-changes-page.service';

@Component({
  selector: 'ui-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@network-changes.title"
      />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <ui-network-not-found />
          } @else {
            @if (service.loggedIn() === false) {
              <p i18n="@@network-changes.login-required">
                The details of network history are available to logged in OpenStreetMap contributors
                only.
              </p>
              <p>
                <ui-user-link-login />
              </p>
            } @else {
              <ui-network-changes />
            }
          }
        </div>
      }
    </ui-page>
  `,
  providers: [NetworkChangesPageService, RouterService],
  imports: [
    NetworkChangesComponent,
    NetworkNotFoundComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    UserLinkLoginComponent,
  ],
})
export class NetworkChangesPageComponent implements OnInit {
  protected readonly service = inject(NetworkChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
