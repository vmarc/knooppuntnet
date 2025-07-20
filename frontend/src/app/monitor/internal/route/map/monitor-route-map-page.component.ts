import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { NavService } from '@app/shared/components/nav.service';
import { OldPageComponent } from '@app/shared/components/page/old-page.component';
import { RouterService } from '@app/shared/services/router.service';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteMapPageService } from './monitor-route-map-page.service';
import { MonitorRouteMapSidebarComponent } from './monitor-route-map-sidebar.component';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';
import { MonitorRouteMapComponent } from './monitor-route-map.component';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'ui-monitor-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <ui-old-page [showFooter]="false">
        <ui-monitor-route-page-header
          pageName="map"
          [groupName]="state.groupName"
          [routeName]="state.routeName"
          [routeDescription]="state.routeDescription"
          [memberCount]="0"
        />

        @if (stateService.page(); as page) {
          @if (!canDisplayMap(page)) {
            <p i18n="@@monitor.route.map.no-map">No map</p>
          } @else {
            <ui-monitor-route-map />
          }
        }

        <ui-monitor-route-map-sidebar sidebar />
      </ui-old-page>
    }
  `,
  providers: [
    MonitorRouteMapPageService,
    MonitorRouteMapService,
    MonitorRouteMapStateService,
    NavService,
    RouterService,
  ],
  imports: [
    MonitorRouteMapComponent,
    MonitorRouteMapSidebarComponent,
    MonitorRoutePageHeaderComponent,
    OldPageComponent,
  ],
})
export class MonitorRouteMapPageComponent {
  readonly service = inject(MonitorRouteMapPageService);
  readonly stateService = inject(MonitorRouteMapStateService);

  canDisplayMap(page: MonitorRouteMapPage): boolean {
    return (
      page &&
      page.bounds &&
      (page.bounds.minLat !== 0 ||
        page.bounds.minLon !== 0 ||
        page.bounds.maxLat !== 0 ||
        page.bounds.maxLon !== 0)
    );
  }
}
