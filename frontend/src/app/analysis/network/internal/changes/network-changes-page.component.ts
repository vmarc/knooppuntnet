import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkChangesComponent } from './components/network-changes.component';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { NetworkChangesPageService } from './network-changes-page.service';

@Component({
  selector: 'ui-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        @if (response.result) {
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
  `,
  providers: [NetworkChangesPageService],
  imports: [NetworkChangesComponent, UserLinkLoginComponent],
})
export class NetworkChangesPageComponent implements OnInit {
  protected readonly service = inject(NetworkChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
