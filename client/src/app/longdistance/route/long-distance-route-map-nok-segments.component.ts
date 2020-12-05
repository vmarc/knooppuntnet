import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {actionLongDistanceRouteMapFocus} from '../../core/longdistance/long-distance.actions';
import {selectLongDistanceRouteMapFocusNokSegmentId} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapNokSegments} from '../../core/longdistance/long-distance.selectors';
import {LongDistanceRouteNokSegment} from '../../kpn/api/common/longdistance/long-distance-route-nok-segment';

@Component({
  selector: 'kpn-long-distance-route-map-nok-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segments">
      <p>
        <span>Deviating segments</span>
      </p>
      <table class="segment-table">
        <tr>
          <th>
            <svg height="30" width="70">
              <line x1="0" y1="15" x2="60" y2="15" style="stroke:red;stroke-width:3"/>
            </svg>
          </th>
          <th>Deviation</th>
          <th class="segment-distance">Length</th>
        </tr>

        <tr *ngFor="let segment of segments$ | async" [ngClass]="{'selected': (nokSegmentId$ | async) == segment.id}">
          <td>
            <button (click)="focus(segment)">Focus</button>
          </td>
          <td>
            {{segment.distance | distance}}
          </td>
          <td class="segment-distance">
            {{segment.meters | distance}}
          </td>
        </tr>
      </table>
    </div>
  `,
  styles: [`

    .segments {
      padding: 15px;
      border-bottom: 1px solid lightgray;
    }

    .selected {
      background-color: #eeeeee;
    }

    .segment-table {
      border-spacing: 0;
      border-collapse: separate;
    }

    .segment-table td {
      padding: 3px;
    }

    .segment-distance {
      text-align: left;
      width: 100%;
    }
  `]
})
export class LongDistanceRouteMapNokSegmentsComponent {

  segments$ = this.store.select(selectLongDistanceRouteMapNokSegments);
  nokSegmentId$ = this.store.select(selectLongDistanceRouteMapFocusNokSegmentId);

  constructor(private store: Store<AppState>) {
  }

  focus(segment: LongDistanceRouteNokSegment): void {
    this.store.dispatch(actionLongDistanceRouteMapFocus({segmentId: segment.id, bounds: segment.bounds}));
  }

}
