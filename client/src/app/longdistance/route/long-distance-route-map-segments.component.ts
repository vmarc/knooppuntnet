import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatSelectionListChange} from '@angular/material/list';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {actionLongDistanceRouteMapFocus} from '../../core/longdistance/long-distance.actions';
import {selectLongDistanceRouteMapOsmSegmentCount} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapOsmSegments} from '../../core/longdistance/long-distance.selectors';
import {LongDistanceRouteNokSegment} from '../../kpn/api/common/longdistance/long-distance-route-nok-segment';
import {LongDistanceRouteMapService} from './long-distance-route-map.service';

@Component({
  selector: 'kpn-long-distance-route-map-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-selection-list [multiple]="false" (selectionChange)="selectionChanged($event)">
      <mat-list-option *ngFor="let segment of segments$ | async" [value]="segment">
        <div class="segment">
          <span class="segment-id">{{segment.id}}</span>
          <span class="segment-legend">
            <kpn-legend-line [color]="segmentColor(segment)"></kpn-legend-line>
          </span>
          <span>{{segment.meters | distance}}</span>
        </div>
      </mat-list-option>
    </mat-selection-list>
  `,
  styles: [`

    .segment {
      display: flex;
    }

    .segment-id {
      width: 2em;
    }

    .segment-legend {
      width: 3em;
    }

  `]
})
export class LongDistanceRouteMapSegmentsComponent {

  segments$ = this.store.select(selectLongDistanceRouteMapOsmSegments);
  segmentCount$ = this.store.select(selectLongDistanceRouteMapOsmSegmentCount);

  constructor(private mapService: LongDistanceRouteMapService,
              private store: Store<AppState>) {
  }

  selectionChanged(event: MatSelectionListChange): void {
    if (event.options.length > 0) {
      const segment: LongDistanceRouteNokSegment = event.options[0].value;
      this.store.dispatch(actionLongDistanceRouteMapFocus({segmentId: segment.id, bounds: segment.bounds}));
    }
  }

  segmentColor(segment: LongDistanceRouteNokSegment): string {
    return this.mapService.colorForSegmentId(segment.id);
  }

}
