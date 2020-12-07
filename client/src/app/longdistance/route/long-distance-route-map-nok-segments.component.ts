import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatSelectionListChange} from '@angular/material/list';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {actionLongDistanceRouteMapFocus} from '../../core/longdistance/long-distance.actions';
import {selectLongDistanceRouteMapGpxEnabled} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapNokSegments} from '../../core/longdistance/long-distance.selectors';
import {LongDistanceRouteNokSegment} from '../../kpn/api/common/longdistance/long-distance-route-nok-segment';

@Component({
  selector: 'kpn-long-distance-route-map-nok-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="gpxTraceAvailable$ | async; then gpxTrace else noGpxTrace"></div>
    <ng-template #noGpxTrace>
      <p>
        No GPX, no known deviations
      </p>
    </ng-template>
    <ng-template #gpxTrace>
      <p class="segments-title">
        <span>GPX segments where OSM is deviating</span>
      </p>

      <div class="segment segment-header">
        <span class="segment-id">
          <kpn-legend-line color="red"></kpn-legend-line>
        </span>
        <span class="segment-deviation">Deviation</span>
        <span>Length</span>
      </div>

      <mat-selection-list [multiple]="false" (selectionChange)="selectionChanged($event)">
        <mat-list-option *ngFor="let segment of segments$ | async" [value]="segment">
          <div class="segment">
            <span class="segment-id">{{segment.id}}</span>
            <span class="segment-deviation">{{segment.distance | distance}}</span>
            <span>{{segment.meters | distance}}</span>
          </div>
        </mat-list-option>
      </mat-selection-list>
    </ng-template>
  `,
  styles: [`

    .segments-title {
      padding-top: 1em;
    }

    .segment {
      display: flex;
    }

    .segment-header {
      padding-left: 1em;
    }

    .segment-id {
      width: 3em;
    }

    .segment-deviation {
      width: 5em;
    }

  `]
})
export class LongDistanceRouteMapNokSegmentsComponent {

  segments$ = this.store.select(selectLongDistanceRouteMapNokSegments);

  gpxTraceAvailable$ = this.store.select(selectLongDistanceRouteMapGpxEnabled);

  constructor(private store: Store<AppState>) {
  }

  selectionChanged(event: MatSelectionListChange): void {
    if (event.options.length > 0) {
      const segment: LongDistanceRouteNokSegment = event.options[0].value;
      this.store.dispatch(actionLongDistanceRouteMapFocus({bounds: segment.bounds}));
    }
  }

}
