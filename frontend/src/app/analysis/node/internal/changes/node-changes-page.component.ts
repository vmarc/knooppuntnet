import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NodeChangesComponent } from '@app/analysis/node/internal/changes/components/node-changes.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { NodeChangesPageService } from './node-changes-page.service';

@Component({
  selector: 'ui-node-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
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
          @if (response.result) {
            <ui-node-changes />
          }
        }
      </div>
    }
  `,
  providers: [NodeChangesPageService, RouterService],
  imports: [NodeChangesComponent, UserLinkLoginComponent],
})
export class NodeChangesPageComponent implements OnInit {
  protected readonly service = inject(NodeChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
