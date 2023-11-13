import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TimestampPipe } from '@app/components/shared/format';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapControlJosmComponent } from './monitor-route-map-control-josm.component';
import { MonitorRouteMapControlModeComponent } from './monitor-route-map-control-mode.component';
import { MonitorRouteMapDeviationsComponent } from './monitor-route-map-deviations.component';
import { MonitorRouteMapLayersComponent } from './monitor-route-map-layers.component';
import { MonitorRouteMapOsmSegmentsComponent } from './monitor-route-map-osm-segments.component';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'kpn-monitor-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div *ngIf="service.page() !== null" class="control">
        <kpn-monitor-route-map-control-mode />
        <kpn-monitor-route-map-layers />
        <kpn-monitor-route-map-control-josm />
        <kpn-monitor-route-map-deviations
          *ngIf="service.mode() === MonitorMapMode.comparison"
        />
        <kpn-monitor-route-map-osm-segments
          *ngIf="service.mode() === MonitorMapMode.osmSegments"
        />
      </div>
      <div
        body-bottom
        *ngIf="service.analysisTimestamp()"
        class="analysis-timestamp"
      >
        <span class="kpn-label">Latest analysis</span>
        <span>{{ service.analysisTimestamp() | yyyymmddhhmm }}</span>
      </div>
    </kpn-sidebar>
  `,
  styles: [
    `
      .control {
        padding: 1em;
      }

      .analysis-timestamp {
        padding: 3em 1em 1em;
        text-align: center;
      }
    `,
  ],
  standalone: true,
  imports: [
    MonitorRouteMapControlJosmComponent,
    MonitorRouteMapControlModeComponent,
    MonitorRouteMapDeviationsComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapOsmSegmentsComponent,
    NgIf,
    SidebarComponent,
    TimestampPipe,
  ],
})
export class MonitorRouteMapSidebarComponent {
  constructor(protected service: MonitorRouteMapStateService) {}

  protected readonly MonitorMapMode = MonitorMapMode;
}
