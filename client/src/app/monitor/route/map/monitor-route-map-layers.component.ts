import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatRadioChange } from '@angular/material/radio/radio';
import { select } from '@ngrx/store';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteMapOsmRelationVisible } from '../../store/monitor.actions';
import { actionMonitorRouteMapNokVisible } from '../../store/monitor.actions';
import { actionMonitorRouteMapOkVisible } from '../../store/monitor.actions';
import { actionMonitorRouteMapReferenceVisible } from '../../store/monitor.actions';
import { actionMonitorRouteMapMode } from '../../store/monitor.actions';
import { selectMonitorRouteMapOsmRelationEnabled } from '../../store/monitor.selectors';
import { selectMonitorRouteMapGpxNokEnabled } from '../../store/monitor.selectors';
import { selectMonitorRouteMapGpxOkEnabled } from '../../store/monitor.selectors';
import { selectMonitorRouteMapGpxEnabled } from '../../store/monitor.selectors';
import { selectMonitorRouteMapOsmRelationVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapGpxNokVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapGpxOkVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapGpxVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapMode } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-map-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode$ | async as mode" class="map-layers">
      <mat-checkbox
        [checked]="gpxVisible$ | async"
        [disabled]="gpxDisabled$ | async"
        (change)="gpxVisibleChanged($event)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="blue"></kpn-legend-line>
          <span>GPX trace</span>
        </div>
      </mat-checkbox>

      <mat-checkbox
        [checked]="gpxOkVisible$ | async"
        [disabled]="gpxOkDisabled$ | async"
        (change)="gpxOkVisibleChanged($event)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="green"></kpn-legend-line>
          <span>GPX same as OSM</span>
        </div>
      </mat-checkbox>

      <mat-checkbox
        [checked]="gpxNokVisible$ | async"
        [disabled]="gpxNokDisabled$ | async"
        (change)="gpxNokVisibleChanged($event)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="red"></kpn-legend-line>
          <span>GPX where OSM is deviating</span>
        </div>
      </mat-checkbox>

      <mat-checkbox
        [checked]="osmRelationVisible$ | async"
        [disabled]="osmRelationDisabled$ | async"
        (change)="osmRelationVisibleChanged($event)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="gold"></kpn-legend-line>
          <span>OSM relation</span>
        </div>
      </mat-checkbox>
    </div>
  `,
  styles: [
    `
      .map-layers {
        margin-top: 1em;
      }

      mat-radio-button {
        display: block;
        padding-bottom: 10px;
      }
    `,
  ],
})
export class MonitorRouteMapLayersComponent {
  readonly mode$ = this.store.select(selectMonitorRouteMapMode);

  readonly gpxVisible$ = this.store.select(selectMonitorRouteMapGpxVisible);
  readonly gpxOkVisible$ = this.store.select(selectMonitorRouteMapGpxOkVisible);
  readonly gpxNokVisible$ = this.store.select(
    selectMonitorRouteMapGpxNokVisible
  );
  readonly osmRelationVisible$ = this.store.select(
    selectMonitorRouteMapOsmRelationVisible
  );

  readonly gpxDisabled$ = this.store.pipe(
    select(selectMonitorRouteMapGpxEnabled),
    map((e) => e === false)
  );
  readonly gpxOkDisabled$ = this.store.pipe(
    select(selectMonitorRouteMapGpxOkEnabled),
    map((e) => e === false)
  );
  readonly gpxNokDisabled$ = this.store.pipe(
    select(selectMonitorRouteMapGpxNokEnabled),
    map((e) => e === false)
  );
  readonly osmRelationDisabled$ = this.store.pipe(
    select(selectMonitorRouteMapOsmRelationEnabled),
    map((e) => e === false)
  );

  constructor(private store: Store<AppState>) {}

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionMonitorRouteMapMode({ mode: event.value }));
  }

  gpxVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapReferenceVisible({ visible: event.checked })
    );
  }

  gpxOkVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapOkVisible({ visible: event.checked })
    );
  }

  gpxNokVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapNokVisible({ visible: event.checked })
    );
  }

  osmRelationVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapOsmRelationVisible({ visible: event.checked })
    );
  }
}
