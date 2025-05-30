import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TimestampPipe } from '@app/shared/components/format/timestamp-pipe';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapControlJosmComponent } from './monitor-route-map-control-josm.component';
import { MonitorRouteMapControlModeComponent } from './monitor-route-map-control-mode.component';
import { MonitorRouteMapDeviationsComponent } from './monitor-route-map-deviations.component';
import { MonitorRouteMapLayersComponent } from './monitor-route-map-layers.component';
import { MonitorRouteMapOsmSegmentsComponent } from './monitor-route-map-osm-segments.component';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'ui-monitor-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-sidebar>
      @if (service.page() !== null) {
        <div class="control">
          @if (service.analysisTimestamp(); as timestamp) {
            <div class="analysis-timestamp">
              <span class="kpn-label">Latest analysis</span>
              <span>{{ timestamp | yyyymmddhhmm }}</span>
            </div>
          }
          <ui-monitor-route-map-control-mode />
          <ui-monitor-route-map-layers />
          <ui-monitor-route-map-control-josm />
          @switch (service.mode()) {
            @case (MonitorMapMode.comparison) {
              <ui-monitor-route-map-deviations />
            }
            @case (MonitorMapMode.osmSegments) {
              <ui-monitor-route-map-osm-segments />
            }
          }
        </div>
      }
    </ui-sidebar>
  `,
  styles: `
    .control {
      padding: 1em;
    }

    .analysis-timestamp {
      padding-bottom: 1em;
    }
  `,
  imports: [
    MonitorRouteMapControlJosmComponent,
    MonitorRouteMapControlModeComponent,
    MonitorRouteMapDeviationsComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapOsmSegmentsComponent,
    SidebarComponent,
    TimestampPipe,
    TimestampPipe,
  ],
})
export class MonitorRouteMapSidebarComponent {
  readonly service = inject(MonitorRouteMapStateService);

  readonly MonitorMapMode = MonitorMapMode;
}
