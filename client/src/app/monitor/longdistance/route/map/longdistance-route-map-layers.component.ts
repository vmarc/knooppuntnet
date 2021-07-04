import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatRadioChange } from '@angular/material/radio/radio';
import { select } from '@ngrx/store';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { actionLongdistanceRouteMapOsmRelationVisible } from '../../../store/monitor.actions';
import { actionLongdistanceRouteMapNokVisible } from '../../../store/monitor.actions';
import { actionLongdistanceRouteMapOkVisible } from '../../../store/monitor.actions';
import { actionLongdistanceRouteMapReferenceVisible } from '../../../store/monitor.actions';
import { actionLongdistanceRouteMapMode } from '../../../store/monitor.actions';
import { selectLongdistanceRouteMapOsmRelationEnabled } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxNokEnabled } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxOkEnabled } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxEnabled } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapOsmRelationVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxNokVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxOkVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapMode } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-longdistance-route-map-layers',
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
          <kpn-legend-line color="yellow"></kpn-legend-line>
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
export class LongdistanceRouteMapLayersComponent {
  readonly mode$ = this.store.select(selectLongdistanceRouteMapMode);

  readonly gpxVisible$ = this.store.select(
    selectLongdistanceRouteMapGpxVisible
  );
  readonly gpxOkVisible$ = this.store.select(
    selectLongdistanceRouteMapGpxOkVisible
  );
  readonly gpxNokVisible$ = this.store.select(
    selectLongdistanceRouteMapGpxNokVisible
  );
  readonly osmRelationVisible$ = this.store.select(
    selectLongdistanceRouteMapOsmRelationVisible
  );

  readonly gpxDisabled$ = this.store.pipe(
    select(selectLongdistanceRouteMapGpxEnabled),
    map((e) => e === false)
  );
  readonly gpxOkDisabled$ = this.store.pipe(
    select(selectLongdistanceRouteMapGpxOkEnabled),
    map((e) => e === false)
  );
  readonly gpxNokDisabled$ = this.store.pipe(
    select(selectLongdistanceRouteMapGpxNokEnabled),
    map((e) => e === false)
  );
  readonly osmRelationDisabled$ = this.store.pipe(
    select(selectLongdistanceRouteMapOsmRelationEnabled),
    map((e) => e === false)
  );

  constructor(private store: Store<AppState>) {}

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionLongdistanceRouteMapMode({ mode: event.value }));
  }

  gpxVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionLongdistanceRouteMapReferenceVisible({ visible: event.checked })
    );
  }

  gpxOkVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionLongdistanceRouteMapOkVisible({ visible: event.checked })
    );
  }

  gpxNokVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionLongdistanceRouteMapNokVisible({ visible: event.checked })
    );
  }

  osmRelationVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionLongdistanceRouteMapOsmRelationVisible({ visible: event.checked })
    );
  }
}
