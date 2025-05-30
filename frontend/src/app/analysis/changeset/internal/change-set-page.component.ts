import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { PageComponent } from '@app/shared/components/page/page.component';
import { Util } from '@app/shared/components/util';
import { RouterService } from '@app/shared/services/router.service';
import { ChangeSetPageService } from './change-set-page.service';
import { ChangeSetHeaderComponent } from './components/change-set-header.component';
import { ChangeSetLocationChangesComponent } from './components/change-set-location-changes.component';
import { ChangeSetNetworkChangesComponent } from './components/change-set-network-changes.component';
import { ChangeSetOrphanNodeChangesComponent } from './components/change-set-orphan-node-changes.component';
import { ChangeSetOrphanRouteChangesComponent } from './components/change-set-orphan-route-changes.component';

@Component({
  selector: 'ui-change-set-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
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
            <ui-change-set-header [detail]="detail" />
            <ui-change-set-location-changes [changess]="detail.summary.locationChanges" />
            <ui-change-set-network-changes [detail]="detail" />
            <ui-change-set-orphan-node-changes [detail]="detail" />
            <ui-change-set-orphan-route-changes [detail]="detail" />
          }
        }
      }
    </ui-page>
  `,
  providers: [ChangeSetPageService, RouterService],
  imports: [
    ChangeSetHeaderComponent,
    ChangeSetLocationChangesComponent,
    ChangeSetNetworkChangesComponent,
    ChangeSetOrphanNodeChangesComponent,
    ChangeSetOrphanRouteChangesComponent,
    MatDivider,
    PageComponent,
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
