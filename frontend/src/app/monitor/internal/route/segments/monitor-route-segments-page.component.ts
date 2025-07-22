import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SuperSegment } from '@api/common/route/super-segment';
import { RouteSegmentListComponent } from '@app/shared/components/route/segment/route-segment-list.component';
import { MonitorRouteSegmentsPageService } from './monitor-route-segments-page.service';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';

@Component({
  selector: 'ui-monitor-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-monitor-route-page-header pageName="segments" />
      @if (service.response(); as response) {
        @if (response) {
          @if (!response.result) {
            <div class="kpn-error" i18n="@@monitor.route.details.not-found">Route not found</div>
          }
          @if (response.result; as page) {
            <ui-route-segment-list
              [segments]="page.superSegments"
              [selectedSegment]="selectedSegment()"
              (selectChange)="selectSegment($event)"
            />
          }
        }
      }
    </ui-page>
  `,
  providers: [MonitorRouteSegmentsPageService, NavService],
  imports: [MonitorRoutePageHeaderComponent, PageComponent, RouteSegmentListComponent],
})
export class MonitorRouteSegmentsPageComponent {
  readonly service = inject(MonitorRouteSegmentsPageService);
  readonly selectedSegment = signal<SuperSegment | undefined>(undefined);

  selectSegment(segment: SuperSegment): void {
    this.service.selectSegment(segment);
  }
}
