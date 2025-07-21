import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteSegmentsPageService } from './monitor-route-segments-page.service';
import { RouteDetailsService } from '@app/route/route-details-service';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';

@Component({
  selector: 'ui-monitor-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.pageState(); as state) {
      <ui-page>
        <ui-monitor-route-page-header pageName="segments" [summary]="state.summary" />

        <ui-monitor-admin-toggle />
      </ui-page>

      @if (state.response; as response) {
        @if (!response.result) {
          <div class="kpn-error" i18n="@@monitor.route.details.not-found">Route not found</div>
        }
        @if (response.result; as page) {
          <pre>{{ debug() }}</pre>
        }
      }
    }
  `,
  providers: [RouteDetailsService, MonitorRouteSegmentsPageService, NavService],
  imports: [MonitorAdminToggleComponent, MonitorRoutePageHeaderComponent, PageComponent],
})
export class MonitorRouteSegmentsPageComponent {
  readonly service = inject(MonitorRouteSegmentsPageService);
  readonly debug = computed(() => JSON.stringify(this.service.pageState(), null, 2));
}
