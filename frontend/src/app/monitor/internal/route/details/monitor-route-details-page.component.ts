import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteDetailsComponent } from '@app/route/route-details.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { MarkdownComponent } from 'ngx-markdown';
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
    @if (response.hasValue()) {
      @if (response.value().result; as page) {
        <ui-route-details
          [situationOn]="response.value().situationOn"
          [routeDetails]="page.details"
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

        @if (page.summary.relationId) {
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
  `,
  providers: [MonitorRouteDetailsPageService],
  imports: [
    DataComponent,
    MarkdownComponent,
    MonitorRouteDetailsAnalysisComponent,
    MonitorRouteDetailsReferenceComponent,
    MonitorRouteDetailsSummaryComponent,
    MonitorRouteDetailsTimestampComponent,
    RouteDetailsComponent,
  ],
})
export class MonitorRouteDetailsPageComponent {
  readonly service = inject(MonitorRouteDetailsPageService);
  readonly response = this.service.response;
}
