import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
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
})
export class MonitorRouteMapControlComponent {
  readonly mode$ = this.store.select(selectMonitorRouteMapMode);

  constructor(private store: Store) {}
}
