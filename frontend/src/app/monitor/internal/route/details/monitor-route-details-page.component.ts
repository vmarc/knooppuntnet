import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteDetailsService } from '@app/route/route-details-service';
import { RouteDetailsComponent } from '@app/route/route-details.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MarkdownComponent } from 'ngx-markdown';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteDetailsAnalysisComponent } from './monitor-route-details-analysis.component';
import { MonitorRouteDetailsPageService } from './monitor-route-details-page.service';
import { MonitorRouteDetailsReferenceComponent } from './monitor-route-details-reference.component';
import { MonitorRouteDetailsSummaryComponent } from './monitor-route-details-summary.component';
import { MonitorRouteDetailsTimestampComponent } from './monitor-route-details-timestamp.component';

@Component({
  selector: 'ui-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    @if (service.pageState(); as state) {
      <ui-page>
        <ui-monitor-route-page-header
          pageName="details"
          [groupName]="state.groupName"
          [routeName]="state.routeName"
          [routeDescription]="state.routeDescription"
        />

        <ui-monitor-admin-toggle />
      </ui-page>

      @if (state.response; as response) {
        @if (!response.result) {
          <div class="kpn-error" i18n="@@monitor.route.details.not-found">Route not found</div>
        }

        @if (response.result; as page) {
          <ui-route-details
            [situationOn]="response.situationOn"
            [routeDetailsData]="page.details"
          />

          <ui-data title="Summary" i18n-title="@@monitor.route.details.summary">
            <ui-monitor-route-details-summary [page]="page" />
          </ui-data>

          <ui-data title="Analysis" i18n-title="@@monitor.route.details.analysis">
            <ui-monitor-route-details-timestamp [page]="page" />
          </ui-data>

          <ui-data title="Reference" i18n-title="@@monitor.route.details.reference">
            <ui-monitor-route-details-reference [page]="page" />
          </ui-data>

          @if (page.relationId) {
            <ui-data title="Analysis" i18n-title="@@monitor.route.details.analysis">
              <ui-monitor-route-details-analysis [page]="page" />
            </ui-data>
          }
          @if (page.comment) {
            <ui-data title="Comment" i18n-title="@@monitor.route.details.comment">
              <markdown [data]="page.comment" />
            </ui-data>
          }
        }
      }
    }
  `,
  styles: `
    .structure {
      padding-top: 1em;
    }
  `,
  providers: [RouteDetailsService, MonitorRouteDetailsPageService, NavService],
  imports: [
    DataComponent,
    MarkdownComponent,
    MonitorAdminToggleComponent,
    MonitorRouteDetailsAnalysisComponent,
    MonitorRouteDetailsReferenceComponent,
    MonitorRouteDetailsSummaryComponent,
    MonitorRouteDetailsTimestampComponent,
    MonitorRoutePageHeaderComponent,
    PageComponent,
    RouteDetailsComponent,
  ],
})
export class MonitorRouteDetailsPageComponent {
  readonly service = inject(MonitorRouteDetailsPageService);
}
