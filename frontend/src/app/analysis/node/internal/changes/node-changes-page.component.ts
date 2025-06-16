import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NodeChangesComponent } from '@app/analysis/node/internal/changes/components/node-changes.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeChangesPageService } from './node-changes-page.service';

@Component({
  selector: 'ui-node-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-node-page-header pageName="changes" />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@node.node-not-found">Node not found</p>
          } @else {
            @if (service.loggedIn() === false) {
              <div>
                <p i18n="@@node.login-required">
                  The details of the node changes history is available to logged in OpenStreetMap
                  contributors only.
                </p>
                <p>
                  <ui-user-link-login />
                </p>
              </div>
            } @else {
              @if (response.result; as page) {
                <ui-node-changes />
              }
            }
          }
        </div>
      }
    </ui-page>
  `,
  providers: [NodeChangesPageService, RouterService],
  imports: [
    ErrorComponent,
    NodeChangesComponent,
    NodePageHeaderComponent,
    PageComponent,
    UserLinkLoginComponent,
  ],
})
export class NodeChangesPageComponent implements OnInit {
  protected readonly service = inject(NodeChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
