import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { Store } from '@ngrx/store';
import { MonitorMapMode } from './monitor-map-mode';
import { actionMonitorRouteMapMode } from './store/monitor-route-map.actions';
import { selectMonitorRouteMapModeSelectionEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmSegmentCount } from './store/monitor-route-map.selectors';

@Component({
  selector: 'kpn-monitor-route-map-control-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="modeSelectionEnabled$ | async">
      <mat-radio-group [value]="mode" (change)="modeChanged($event)">
        <mat-radio-button value="comparison">
          <span i18n="@@monitor.route.map.mode.comparison">
            GPX / OSM comparison
          </span>
        </mat-radio-button>
        <mat-radio-button value="osm-segments">
          <span i18n="@@monitor.route.map.mode.osm-segments">OSM segments</span>
          <span class="kpn-brackets">{{ osmSegmentCount$ | async }}</span>
        </mat-radio-button>
      </mat-radio-group>
    </div>
  `,
  standalone: true,
  imports: [NgIf, MatRadioModule, AsyncPipe],
})
export class MonitorRouteMapControlModeComponent {
  @Input() mode: MonitorMapMode;

  readonly osmSegmentCount$ = this.store.select(
    selectMonitorRouteMapOsmSegmentCount
  );

  readonly modeSelectionEnabled$ = this.store.select(
    selectMonitorRouteMapModeSelectionEnabled
  );

  constructor(private store: Store) {}

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionMonitorRouteMapMode({ mapMode: event.value }));
  }
}
