import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionMonitorRouteDetailsPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteDetailsPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDetailsPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header pageName="details" />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@monitor.route.details.not-found">
        Route not found
      </div>

      <div *ngIf="response.result as page">
        <kpn-data title="Summary" i18n-title="@@monitor.route.details.summary">
          <kpn-monitor-route-details-summary [page]="page" />
        </kpn-data>

        <kpn-data
          title="Reference"
          i18n-title="@@monitor.route.details.reference"
        >
          <kpn-monitor-route-details-reference [page]="page" />
        </kpn-data>

        <kpn-data
          *ngIf="page.relationId"
          title="Analysis"
          i18n-title="@@monitor.route.details.analysis"
        >
          <kpn-monitor-route-details-analysis [page]="page" />
        </kpn-data>

        <kpn-data
          *ngIf="page.comment"
          title="Comment"
          i18n-title="@@monitor.route.details.comment"
        >
          <markdown [data]="page.comment" />
        </kpn-data>

        <kpn-data
          *ngIf="page.structureRows?.length > 0"
          title="Structure"
          i18n-title="@@monitor.route.details.structure"
        />
        <div class="structure">
          <kpn-monitor-route-details-structure
            [groupName]="page.groupName"
            [routeName]="page.routeName"
            [structureRows]="page.structureRows"
            [referenceType]="page.referenceType"
          />
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .structure {
        padding-top: 1em;
      }
    `,
  ],
})
export class MonitorRouteDetailsPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectMonitorRouteDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageDestroy());
  }

  gpxUpload(): void {}

  gpxDownload(): void {}
}
