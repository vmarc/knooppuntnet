import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {MatRadioChange} from '@angular/material/radio/radio';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {map} from 'rxjs/operators';
import {AppState} from '../../core/core.state';
import {actionLongDistanceRouteMapOsmRelationVisible} from '../../core/longdistance/long-distance.actions';
import {actionLongDistanceRouteMapGpxNokVisible} from '../../core/longdistance/long-distance.actions';
import {actionLongDistanceRouteMapGpxOkVisible} from '../../core/longdistance/long-distance.actions';
import {actionLongDistanceRouteMapGpxVisible} from '../../core/longdistance/long-distance.actions';
import {actionLongDistanceRouteMapMode} from '../../core/longdistance/long-distance.actions';
import {selectLongDistanceRouteMapOsmRelationEnabled} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxNokEnabled} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxOkEnabled} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxEnabled} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapOsmRelationVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxNokVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxOkVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapMode} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-map-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode$ | async as mode" class="map-layers">

      <mat-checkbox
        [checked]="gpxVisible$ | async"
        [disabled]="gpxDisabled$ | async"
        (change)="gpxVisibleChanged($event)">
        <div class="kpn-line">
          <kpn-legend-line color="blue"></kpn-legend-line>
          <span>GPX trace</span>
        </div>
      </mat-checkbox>

      <mat-checkbox
        [checked]="gpxOkVisible$ | async"
        [disabled]="gpxOkDisabled$ | async"
        (change)="gpxOkVisibleChanged($event)">
        <div class="kpn-line">
          <kpn-legend-line color="green"></kpn-legend-line>
          <span>GPX same as OSM</span>
        </div>
      </mat-checkbox>

      <mat-checkbox
        [checked]="gpxNokVisible$ | async"
        [disabled]="gpxNokDisabled$ | async"
        (change)="gpxNokVisibleChanged($event)">
        <div class="kpn-line">
          <kpn-legend-line color="red"></kpn-legend-line>
          <span>GPX where OSM is deviating</span>
        </div>
      </mat-checkbox>

      <mat-checkbox
        [checked]="osmRelationVisible$ | async"
        [disabled]="osmRelationDisabled$ | async"
        (change)="osmRelationVisibleChanged($event)">
        <div class="kpn-line">
          <kpn-legend-line color="yellow"></kpn-legend-line>
          <span>OSM relation</span>
        </div>
      </mat-checkbox>
    </div>
  `,
  styles: [`

    .map-layers {
      margin-top: 1em;
    }

    mat-radio-button {
      display: block;
      padding-bottom: 10px;
    }

  `]
})
export class LongDistanceRouteMapLayersComponent {

  mode$ = this.store.select(selectLongDistanceRouteMapMode);

  gpxVisible$ = this.store.select(selectLongDistanceRouteMapGpxVisible);
  gpxOkVisible$ = this.store.select(selectLongDistanceRouteMapGpxOkVisible);
  gpxNokVisible$ = this.store.select(selectLongDistanceRouteMapGpxNokVisible);
  osmRelationVisible$ = this.store.select(selectLongDistanceRouteMapOsmRelationVisible);

  gpxDisabled$ = this.store.pipe(select(selectLongDistanceRouteMapGpxEnabled), map(e => e === false));
  gpxOkDisabled$ = this.store.pipe(select(selectLongDistanceRouteMapGpxOkEnabled), map(e => e === false));
  gpxNokDisabled$ = this.store.pipe(select(selectLongDistanceRouteMapGpxNokEnabled), map(e => e === false));
  osmRelationDisabled$ = this.store.pipe(select(selectLongDistanceRouteMapOsmRelationEnabled), map(e => e === false));

  constructor(private store: Store<AppState>) {
  }

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionLongDistanceRouteMapMode({mode: event.value}));
  }

  gpxVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(actionLongDistanceRouteMapGpxVisible({visible: event.checked}));
  }

  gpxOkVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(actionLongDistanceRouteMapGpxOkVisible({visible: event.checked}));
  }

  gpxNokVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(actionLongDistanceRouteMapGpxNokVisible({visible: event.checked}));
  }

  osmRelationVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(actionLongDistanceRouteMapOsmRelationVisible({visible: event.checked}));
  }
}
