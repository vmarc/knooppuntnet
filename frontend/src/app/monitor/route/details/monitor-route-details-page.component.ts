import { inject } from '@angular/core';
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
import { MonitorRouteDetailsTimestampComponent } from './monitor-route-details-timestamp.component';

@Component({
  selector: 'kpn-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <kpn-page>
        <kpn-monitor-route-page-header
          pageName="details"
          [groupName]="state.groupName"
          [routeName]="state.routeName"
          [routeDescription]="state.routeDescription"
        />

        <kpn-monitor-admin-toggle />

        @if (state.response; as response) {
          @if (!response.result) {
            <div class="kpn-error" i18n="@@monitor.route.details.not-found">Route not found</div>
          }

          @if (response.result; as page) {
            <kpn-data title="Summary" i18n-title="@@monitor.route.details.summary">
              <kpn-monitor-route-details-summary [page]="page" />
            </kpn-data>

            <kpn-data title="Latest analysis" i18n-title="@@monitor.route.details.analysis">
              <kpn-monitor-route-details-timestamp [page]="page" />
            </kpn-data>

            <kpn-data title="Reference" i18n-title="@@monitor.route.details.reference">
              <kpn-monitor-route-details-reference [page]="page" />
            </kpn-data>

            @if (page.relationId) {
              <kpn-data title="Analysis" i18n-title="@@monitor.route.details.analysis">
                <kpn-monitor-route-details-analysis [page]="page" />
              </kpn-data>
            }
            @if (page.comment) {
              <kpn-data title="Comment" i18n-title="@@monitor.route.details.comment">
                <markdown [data]="page.comment" />
              </kpn-data>
            }

            @if (page.structureRows) {
              <kpn-data title="Structure" i18n-title="@@monitor.route.details.structure" />
              <div class="structure">
                <kpn-monitor-route-details-structure
                  [admin]="service.admin()"
                  [groupName]="page.groupName"
                  [routeName]="page.routeName"
                  [structureRows]="page.structureRows"
                  [referenceType]="page.referenceType"
                />
              </div>
            }
          }
        }
        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  styles: `
    .structure {
      padding-top: 1em;
    }
  `,
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
    MonitorRouteDetailsTimestampComponent,
    MonitorRoutePageHeaderComponent,
    PageComponent,
    SidebarComponent,
  ],
})
export class MonitorRouteDetailsPageComponent {
  protected readonly service = inject(MonitorRouteDetailsPageService);
}
