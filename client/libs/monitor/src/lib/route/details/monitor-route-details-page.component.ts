import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NavService } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MarkdownModule } from 'ngx-markdown';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteDetailsAnalysisComponent } from './monitor-route-details-analysis.component';
import { MonitorRouteDetailsPageService } from './monitor-route-details-page.service';
import { MonitorRouteDetailsReferenceComponent } from './monitor-route-details-reference.component';
import { MonitorRouteDetailsStructureComponent } from './monitor-route-details-structure.component';
import { MonitorRouteDetailsSummaryComponent } from './monitor-route-details-summary.component';

@Component({
  selector: 'kpn-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page *ngIf="service.state() as state">
      <kpn-monitor-route-page-header
        pageName="details"
        [groupName]="state.groupName"
        [routeName]="state.routeName"
        [routeDescription]="state.routeDescription"
      />

      <kpn-monitor-admin-toggle />

      <div *ngIf="state.response as response" class="kpn-spacer-above">
        <div *ngIf="!response.result" i18n="@@monitor.route.details.not-found">
          Route not found
        </div>

        <div *ngIf="response.result as page">
          <kpn-data
            title="Summary"
            i18n-title="@@monitor.route.details.summary"
          >
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
              [admin]="service.admin()"
              [groupName]="page.groupName"
              [routeName]="page.routeName"
              [structureRows]="page.structureRows"
              [referenceType]="page.referenceType"
            />
          </div>
        </div>
      </div>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  styles: [
    `
      .structure {
        padding-top: 1em;
      }
    `,
  ],
  providers: [MonitorRouteDetailsPageService, NavService],
  standalone: true,
  imports: [
    DataComponent,
    MarkdownModule,
    MonitorAdminToggleComponent,
    MonitorRouteDetailsAnalysisComponent,
    MonitorRouteDetailsReferenceComponent,
    MonitorRouteDetailsStructureComponent,
    MonitorRouteDetailsSummaryComponent,
    MonitorRoutePageHeaderComponent,
    NgIf,
    PageComponent,
    SidebarComponent,
  ],
})
export class MonitorRouteDetailsPageComponent {
  constructor(protected service: MonitorRouteDetailsPageService) {}
}
