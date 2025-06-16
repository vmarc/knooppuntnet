import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouteChangesComponent } from '@app/analysis/route/internal/changes/components/route-changes.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteChangesPageService } from './route-changes-page.service';

@Component({
  selector: 'ui-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="changes" />
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
              @if (response.result; as page) {
                <ui-route-changes />
              }
            }
          }
        </div>
      }
    </ui-page>
  `,
  providers: [RouteChangesPageService, RouterService],
  imports: [PageComponent, RouteChangesComponent, RoutePageHeaderComponent, UserLinkLoginComponent],
})
export class RouteChangesPageComponent implements OnInit {
  protected readonly service = inject(RouteChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
