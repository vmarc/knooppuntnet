import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DataComponent } from '@app/components/shared/data';
import { Store } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { actionMonitorRouteDetailsPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteDetailsPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDetailsPage } from '../../store/monitor.selectors';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteDetailsAnalysisComponent } from './monitor-route-details-analysis.component';
import { MonitorRouteDetailsReferenceComponent } from './monitor-route-details-reference.component';
import { MonitorRouteDetailsStructureComponent } from './monitor-route-details-structure.component';
import { MonitorRouteDetailsSummaryComponent } from './monitor-route-details-summary.component';

@Component({
  selector: 'kpn-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header pageName="details" />

    <kpn-monitor-admin-toggle />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
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
          *ngIf="page.structureRows"
          title="Structure"
          i18n-title="@@monitor.route.details.structure"
        />
        <div *ngIf="page.structureRows" class="structure">
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
  standalone: true,
  imports: [
    MonitorRoutePageHeaderComponent,
    NgIf,
    DataComponent,
    MonitorRouteDetailsSummaryComponent,
    MonitorRouteDetailsReferenceComponent,
    MonitorRouteDetailsAnalysisComponent,
    MarkdownModule,
    MonitorRouteDetailsStructureComponent,
    AsyncPipe,
    MonitorAdminToggleComponent,
  ],
})
export class MonitorRouteDetailsPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectMonitorRouteDetailsPage);

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
