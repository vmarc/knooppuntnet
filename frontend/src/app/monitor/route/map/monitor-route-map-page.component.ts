import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { EditGotoService } from '../../../analysis/components/edit/edit-goto.service';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteMapPageService } from './monitor-route-map-page.service';
import { MonitorRouteMapSidebarComponent } from './monitor-route-map-sidebar.component';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';
import { MonitorRouteMapComponent } from './monitor-route-map.component';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <kpn-page [showFooter]="false">
        <kpn-monitor-route-page-header
          pageName="map"
          [groupName]="state.groupName"
          [routeName]="state.routeName"
          [routeDescription]="state.routeDescription"
          [subRelations]="subRelations()"
          [previous]="previous()"
          [next]="next()"
          (selectSubRelation)="service.selectSubRelation($event)"
          (goHereInJosm)="josm()"
        />

        @if (stateService.page(); as page) {
          @if (!canDisplayMap(page)) {
            <p i18n="@@monitor.route.map.no-map">No map</p>
          } @else {
            <kpn-monitor-route-map />
          }
        }

        <kpn-monitor-route-map-sidebar sidebar />
      </kpn-page>
    }
  `,
  providers: [
    MonitorRouteMapPageService,
    MonitorRouteMapService,
    MonitorRouteMapStateService,
    NavService,
  ],
  standalone: true,
  imports: [
    MonitorRouteMapComponent,
    MonitorRouteMapSidebarComponent,
    MonitorRoutePageHeaderComponent,
    PageComponent,
    SidebarComponent,
  ],
})
export class MonitorRouteMapPageComponent {
  protected readonly service = inject(MonitorRouteMapPageService);
  protected readonly stateService = inject(MonitorRouteMapStateService);
  private readonly editGotoService = inject(EditGotoService);

  readonly subRelations = computed(() => {
    return this.stateService.page()?.subRelations ?? [];
  });
  readonly previous = computed(() => {
    return this.stateService.page()?.previousSubRelation;
  });
  readonly next = computed(() => {
    return this.stateService.page()?.nextSubRelation;
  });

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

  josm(): void {
    const bounds = this.service.mapBounds();
    if (bounds !== null) {
      this.editGotoService.gotoBoundsInJosm(bounds);
    }
  }
}
