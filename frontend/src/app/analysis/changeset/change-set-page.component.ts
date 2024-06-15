import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Util } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { RouterService } from '../../shared/services/router.service';
import { ChangeSetPageService } from './change-set-page.service';
import { ChangeSetHeaderComponent } from './components/change-set-header.component';
import { ChangeSetLocationChangesComponent } from './components/change-set-location-changes.component';
import { ChangeSetNetworkChangesComponent } from './components/change-set-network-changes.component';
import { ChangeSetOrphanNodeChangesComponent } from './components/change-set-orphan-node-changes.component';
import { ChangeSetOrphanRouteChangesComponent } from './components/change-set-orphan-route-changes.component';

@Component({
  selector: 'kpn-change-set-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <h1>
        <ng-container i18n="@@change-set.title">Changeset</ng-container>
        {{ service.changeSetTitle }}
      </h1>

      @if (service.response(); as response) {
        @if (!response.result) {
          <div i18n="@@changeset.not-found">Changeset not found</div>
        } @else {
          @if (response.result.replicationNumbers.length > 1) {
            <p i18n="@@changeset.split" class="kpn-label">
              This changeset is split over multiple minute diffs
            </p>
            <ul>
              @for (
                replicationNumber of response.result.replicationNumbers;
                track replicationNumber
              ) {
                <li>
                  <a [routerLink]="link(replicationNumber)">{{ format(replicationNumber) }}</a>
                </li>
              }
            </ul>
          } @else {
            <kpn-change-set-header [detail]="response.result.detail" />
            <kpn-change-set-location-changes
              [changess]="response.result.detail.summary.locationChanges"
            />
            <kpn-change-set-network-changes [detail]="response.result.detail" />
            <kpn-change-set-orphan-node-changes [detail]="response.result.detail" />
            <kpn-change-set-orphan-route-changes [detail]="response.result.detail" />
          }
        }
      }
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [ChangeSetPageService, RouterService],
  standalone: true,
  imports: [
    ChangeSetHeaderComponent,
    ChangeSetLocationChangesComponent,
    ChangeSetNetworkChangesComponent,
    ChangeSetOrphanNodeChangesComponent,
    ChangeSetOrphanRouteChangesComponent,
    PageComponent,
    SidebarComponent,
    RouterLink,
  ],
})
export class ChangeSetPageComponent implements OnInit {
  protected readonly service = inject(ChangeSetPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  format(replicationNumber: number): string {
    return Util.replicationName(replicationNumber);
  }

  link(replicationNumber: number): string {
    return `/analysis/changeset/${this.service.key.changeSetId}/${replicationNumber}`;
  }
}
