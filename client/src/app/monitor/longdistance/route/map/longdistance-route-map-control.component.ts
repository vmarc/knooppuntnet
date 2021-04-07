import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio/radio';
import { select } from '@ngrx/store';
import { Store } from '@ngrx/store';
import { first } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { actionLongdistanceRouteMapFocus } from '../../../store/monitor.actions';
import { actionLongdistanceRouteMapMode } from '../../../store/monitor.actions';
import { selectLongdistanceRouteMapBounds } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapOsmSegmentCount } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapMode } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-longdistance-route-map-control',
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
            <span class="kpn-brackets">{{ osmSegmentCount$ | async }}</span>
          </mat-radio-button>
        </mat-radio-group>
      </div>

      <kpn-longdistance-route-map-layers></kpn-longdistance-route-map-layers>

      <div class="kpn-spacer-above">
        <button mat-raised-button (click)="zoomToFitRoute()">
          Zoom to fit route
        </button>
      </div>

      <kpn-longdistance-route-map-nok-segments *ngIf="mode === 'comparison'">
      </kpn-longdistance-route-map-nok-segments>

      <kpn-longdistance-route-map-osm-segments *ngIf="mode === 'osm-segments'">
      </kpn-longdistance-route-map-osm-segments>
    </div>
  `,
  styles: [
    `
      .control {
        padding: 1em;
      }

      mat-radio-button {
        display: block;
        padding-bottom: 10px;
      }
    `,
  ],
})
export class LongdistanceRouteMapControlComponent {
  readonly mode$ = this.store.select(selectLongdistanceRouteMapMode);
  readonly osmSegmentCount$ = this.store.select(
    selectLongdistanceRouteMapOsmSegmentCount
  );

  readonly modeSelectionEnabled$ = this.store.pipe(
    select(selectLongdistanceRouteMapOsmSegmentCount),
    map((osmSegmentCount) => osmSegmentCount > 1)
  );

  constructor(private store: Store<AppState>) {}

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionLongdistanceRouteMapMode({ mode: event.value }));
  }

  zoomToFitRoute(): void {
    this.store
      .select(selectLongdistanceRouteMapBounds)
      .pipe(first())
      .subscribe((bounds) => {
        this.store.dispatch(actionLongdistanceRouteMapFocus({ bounds }));
      });
  }
}
