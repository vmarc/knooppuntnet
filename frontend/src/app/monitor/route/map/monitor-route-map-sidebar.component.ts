import { inject } from '@angular/core';
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
      @if (service.page() !== null) {
        <div class="control">
          @if (service.analysisTimestamp(); as timestamp) {
            <div class="analysis-timestamp">
              <span class="kpn-label">Latest analysis</span>
              <span>{{ timestamp | yyyymmddhhmm }}</span>
            </div>
          }
          <kpn-monitor-route-map-control-mode />
          <kpn-monitor-route-map-layers />
          <kpn-monitor-route-map-control-josm />
          @switch (service.mode()) {
            @case (MonitorMapMode.comparison) {
              <kpn-monitor-route-map-deviations />
            }
            @case (MonitorMapMode.osmSegments) {
              <kpn-monitor-route-map-osm-segments />
            }
          }
        </div>
      }
    </kpn-sidebar>
  `,
  styles: `
    .control {
      padding: 1em;
    }

    .analysis-timestamp {
      padding-bottom: 1em;
    }
  `,
  standalone: true,
  imports: [
    MonitorRouteMapControlJosmComponent,
    MonitorRouteMapControlModeComponent,
    MonitorRouteMapDeviationsComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapOsmSegmentsComponent,
    SidebarComponent,
    TimestampPipe,
  ],
})
export class MonitorRouteMapSidebarComponent {
  protected readonly service = inject(MonitorRouteMapStateService);

  protected readonly MonitorMapMode = MonitorMapMode;
}
