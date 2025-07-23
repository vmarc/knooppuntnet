import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouteChangesComponent } from './components/route-changes.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { RouteChangesPageService } from './route-changes-page.service';

@Component({
  selector: 'ui-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        @if (!response.result) {
          <div i18n="@@route.route-not-found">Route not found</div>
        } @else {
          @if (service.loggedIn() === false) {
            <div>
              <p i18n="@@route-changes.login-required">
                The details of the route history is available to logged in OpenStreetMap
                contributors only.
              </p>
              <p>
                <ui-user-link-login />
              </p>
            </div>
          } @else {
            @if (response.result) {
              <ui-route-changes />
            }
          }
        }
      </div>
    }
  `,
  providers: [RouteChangesPageService, RouterService],
  imports: [RouteChangesComponent, UserLinkLoginComponent],
})
export class RouteChangesPageComponent implements OnInit {
  protected readonly service = inject(RouteChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
