import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { Util } from '@app/components/shared';
import { OldPageComponent } from '@app/components/shared/page';
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
    <kpn-old-page>
      <h1>
        <ng-container i18n="@@change-set.title">Changeset</ng-container>
        {{ service.changeSetTitle }}
      </h1>

      @if (service.response(); as response) {
        @if (!response.result) {
          <div i18n="@@changeset.not-found">Changeset not found</div>
        } @else {
          @for (detail of response.result.details; track detail.summary._id; let first = $first) {
            @if (!first) {
              <mat-divider />
            }
            <kpn-change-set-header [detail]="detail" />
            <kpn-change-set-location-changes [changess]="detail.summary.locationChanges" />
            <kpn-change-set-network-changes [detail]="detail" />
            <kpn-change-set-orphan-node-changes [detail]="detail" />
            <kpn-change-set-orphan-route-changes [detail]="detail" />
          }
        }
      }
      <kpn-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [ChangeSetPageService, RouterService],
  imports: [
    ChangeSetHeaderComponent,
    ChangeSetLocationChangesComponent,
    ChangeSetNetworkChangesComponent,
    ChangeSetOrphanNodeChangesComponent,
    ChangeSetOrphanRouteChangesComponent,
    OldPageComponent,
    SidebarComponent,
    MatDivider,
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
