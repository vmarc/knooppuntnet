import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteDetailsPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteDetailsPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDetailsPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header
      pageName="details"
    ></kpn-monitor-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@monitor.route.details.not-found">
        Route not found
      </div>

      <div *ngIf="response.result as route">
        <kpn-data title="Summary" i18n-title="@@monitor.route.details.summary">
          <kpn-monitor-route-details-summary
            [page]="route"
          ></kpn-monitor-route-details-summary>
        </kpn-data>

        <kpn-data
          title="Reference"
          i18n-title="@@monitor.route.details.reference"
        >
          <kpn-monitor-route-details-reference
            [page]="route"
          ></kpn-monitor-route-details-reference>
        </kpn-data>

        <kpn-data
          *ngIf="route.relationId"
          title="Analysis"
          i18n-title="@@monitor.route.details.analysis"
        >
          <kpn-monitor-route-details-analysis
            [page]="route"
          ></kpn-monitor-route-details-analysis>
        </kpn-data>

        <kpn-data
          *ngIf="route.comment"
          title="Comment"
          i18n-title="@@monitor.route.details.comment"
        >
          <markdown [data]="route.comment"></markdown>
        </kpn-data>

        <kpn-data
          *ngIf="route.relation?.relations?.length > 0"
          title="Structure"
          i18n-title="@@monitor.route.details.structure"
        >
        </kpn-data>
        <kpn-monitor-route-details-structure
          *ngIf="route.relation?.relations?.length > 0"
          [relation]="route.relation"
        >
        </kpn-monitor-route-details-structure>
      </div>
    </div>
  `,
})
export class MonitorRouteDetailsPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectMonitorRouteDetailsPage);

  constructor(private snackBar: MatSnackBar, private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageDestroy());
  }

  gpxUpload(): void {
    this.snackBar.open('Sorry, GPX file upload not implemented yet', 'close', {
      panelClass: ['mat-toolbar', 'mat-primary'],
    });
  }

  gpxDownload(): void {
    this.snackBar.open(
      'Sorry, GPX file download not implemented yet',
      'close',
      { panelClass: ['mat-toolbar', 'mat-primary'] }
    );
  }
}
