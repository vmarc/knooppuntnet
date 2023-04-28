import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { MonitorRouteMapControlJosmComponent } from './monitor-route-map-control-josm.component';
import { MonitorRouteMapControlModeComponent } from './monitor-route-map-control-mode.component';
import { MonitorRouteMapDeviationsComponent } from './monitor-route-map-deviations.component';
import { MonitorRouteMapLayersComponent } from './monitor-route-map-layers.component';
import { MonitorRouteMapOsmSegmentsComponent } from './monitor-route-map-osm-segments.component';
import { selectMonitorRouteMapMode } from './store/monitor-route-map.selectors';

@Component({
  selector: 'kpn-monitor-route-map-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode$ | async as mode" class="control">
      <kpn-monitor-route-map-control-mode [mode]="mode" />
      <kpn-monitor-route-map-layers />
      <kpn-monitor-route-map-control-josm />
      <kpn-monitor-route-map-deviations *ngIf="mode === 'comparison'" />
      <kpn-monitor-route-map-osm-segments *ngIf="mode === 'osm-segments'" />
    </div>
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
    NgIf,
    MonitorRouteMapControlModeComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapControlJosmComponent,
    MonitorRouteMapDeviationsComponent,
    MonitorRouteMapOsmSegmentsComponent,
    AsyncPipe,
  ],
})
export class MonitorRouteMapControlComponent {
  readonly mode$ = this.store.select(selectMonitorRouteMapMode);

  constructor(private store: Store) {}
}
