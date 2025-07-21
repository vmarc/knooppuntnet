import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteStructureComponent } from '@app/route/route-structure.component';
import { MonitorRouteMembersPageService } from './monitor-route-members-page.service';
import { RouteDetailsService } from '@app/route/route-details-service';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';

@Component({
  selector: 'ui-monitor-route-members-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.pageState(); as state) {
      <ui-page>
        <ui-monitor-route-page-header pageName="members" />

        <ui-monitor-admin-toggle />
      </ui-page>

      @if (state.response; as response) {
        @if (!response.result) {
          <div class="kpn-error" i18n="@@monitor.route.details.not-found">Route not found</div>
        }
        @if (response.result; as page) {
          <ui-route-structure [routeType]="page.routeTypes[0]" [rows]="page.structureRows" />
        }
      }
    }
  `,
  providers: [RouteDetailsService, MonitorRouteMembersPageService, NavService],
  imports: [
    MonitorAdminToggleComponent,
    MonitorRoutePageHeaderComponent,
    PageComponent,
    RouteStructureComponent,
  ],
})
export class MonitorRouteMembersPageComponent {
  readonly service = inject(MonitorRouteMembersPageService);
}
