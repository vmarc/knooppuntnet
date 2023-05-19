import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorRouteMapControlJosmComponent } from './monitor-route-map-control-josm.component';
import { MonitorRouteMapControlModeComponent } from './monitor-route-map-control-mode.component';
import { MonitorRouteMapDeviationsComponent } from './monitor-route-map-deviations.component';
import { MonitorRouteMapLayersComponent } from './monitor-route-map-layers.component';
import { MonitorRouteMapOsmSegmentsComponent } from './monitor-route-map-osm-segments.component';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div *ngIf="service.page() !== null" class="control">
        <kpn-monitor-route-map-control-mode [mode]="service.mode()" />
        <kpn-monitor-route-map-layers />
        <kpn-monitor-route-map-control-josm />
        <kpn-monitor-route-map-deviations
          *ngIf="service.mode() === 'comparison'"
        />
        <kpn-monitor-route-map-osm-segments
          *ngIf="service.mode() === 'osm-segments'"
        />
      </div>
    </kpn-sidebar>
  `,
  styles: [
    `
      .control {
        padding: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [
    SidebarComponent,
    MonitorRouteMapControlJosmComponent,
    MonitorRouteMapControlModeComponent,
    MonitorRouteMapDeviationsComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapOsmSegmentsComponent,
    NgIf,
  ],
})
export class MonitorRouteMapSidebarComponent {
  constructor(protected service: MonitorRouteMapService) {
    console.log('MonitorRouteMapSidebarComponent.constructor()');
  }
}
