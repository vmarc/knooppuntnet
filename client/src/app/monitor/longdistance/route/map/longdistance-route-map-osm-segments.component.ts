import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatSelectionListChange} from '@angular/material/list';
import {LongdistanceRouteSegment} from '@api/common/monitor/longdistance-route-segment';
import {Store} from '@ngrx/store';
import {AppState} from '../../../../core/core.state';
import {actionLongdistanceRouteMapFocus} from '../../../store/monitor.actions';
import {selectLongdistanceRouteMapOsmSegments} from '../../../store/monitor.selectors';
import {LongdistanceRouteMapService} from './longdistance-route-map.service';

@Component({
  selector: 'kpn-longdistance-route-map-osm-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-selection-list [multiple]="false" (selectionChange)="selectionChanged($event)">
      <mat-list-option *ngFor="let segment of segments$ | async" [value]="segment">
        <div class="segment">
          <span class="segment-id">{{segment.id}}</span>
          <span class="segment-legend">
            <kpn-longdistance-route-map-legend-line [color]="segmentColor(segment)"></kpn-longdistance-route-map-legend-line>
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
export class LongdistanceRouteMapOsmSegmentsComponent {

  readonly segments$ = this.store.select(selectLongdistanceRouteMapOsmSegments);

  constructor(private store: Store<AppState>,
              private mapService: LongdistanceRouteMapService) {
  }

  selectionChanged(event: MatSelectionListChange): void {
    if (event.options.length > 0) {
      const segment: LongdistanceRouteSegment = event.options[0].value;
      this.store.dispatch(actionLongdistanceRouteMapFocus({bounds: segment.bounds}));
    }
  }

  segmentColor(segment: LongdistanceRouteSegment): string {
    return this.mapService.colorForSegmentId(segment.id);
  }

}
