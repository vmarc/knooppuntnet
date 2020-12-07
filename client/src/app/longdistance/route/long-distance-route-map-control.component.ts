import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatRadioChange} from '@angular/material/radio/radio';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {first} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {AppState} from '../../core/core.state';
import {actionLongDistanceRouteMapFocus} from '../../core/longdistance/long-distance.actions';
import {actionLongDistanceRouteMapMode} from '../../core/longdistance/long-distance.actions';
import {selectLongDistanceRouteMapBounds} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapMode} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapOsmSegmentCount} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-map-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode$ | async as mode" class="control">

      <div *ngIf="modeSelectionEnabled$ | async">
        <mat-radio-group [value]="mode" (change)="modeChanged($event)">
          <mat-radio-button value="comparison">
            <span>GPX / OSM comparison</span>
          </mat-radio-button>
          <mat-radio-button value="osm-segments">
            <span>OSM segments</span>
            <span class="kpn-brackets">{{osmSegmentCount$ | async}}</span>
          </mat-radio-button>
        </mat-radio-group>
      </div>

      <kpn-long-distance-route-map-layers></kpn-long-distance-route-map-layers>

      <div class="kpn-spacer-above">
        <button mat-raised-button (click)="zoomToFitRoute()">Zoom to fit route</button>
      </div>

      <kpn-long-distance-route-map-nok-segments *ngIf="mode === 'comparison'">
      </kpn-long-distance-route-map-nok-segments>

      <kpn-long-distance-route-map-osm-segments *ngIf="mode === 'osm-segments'">
      </kpn-long-distance-route-map-osm-segments>

    </div>
  `,
  styles: [`

    .control {
      padding: 1em;
    }

    mat-radio-button {
      display: block;
      padding-bottom: 10px;
    }

  `]
})
export class LongDistanceRouteMapControlComponent {

  mode$ = this.store.select(selectLongDistanceRouteMapMode);
  osmSegmentCount$ = this.store.select(selectLongDistanceRouteMapOsmSegmentCount);

  modeSelectionEnabled$ = this.store.pipe(
    select(selectLongDistanceRouteMapOsmSegmentCount),
    map(osmSegmentCount => osmSegmentCount > 1)
  );

  constructor(private store: Store<AppState>) {
  }

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionLongDistanceRouteMapMode({mode: event.value}));
  }

  zoomToFitRoute(): void {
    this.store.select(selectLongDistanceRouteMapBounds).pipe(first()).subscribe(bounds => {
      this.store.dispatch(actionLongDistanceRouteMapFocus({bounds: bounds}));
    });
  }
}
