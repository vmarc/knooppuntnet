import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteMapNokSegments} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-map-nok-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segments">
      <p>
        <span>Deviating segments</span>
      </p>
      <table>
        <tr>
          <th>
            <svg height="30" width="70">
              <line x1="0" y1="15" x2="60" y2="15" style="stroke:red;stroke-width:3"/>
            </svg>
          </th>
          <th>Deviation</th>
          <th>Length</th>
        </tr>
        <tr *ngFor="let segment of segments$ | async">
          <td>
            <button>Zoom</button>
          </td>
          <td>
            {{segment.distance | distance}}
          </td>
          <td>
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
  `]
})
export class LongDistanceRouteMapNokSegmentsComponent {

  segments$ = this.store.select(selectLongDistanceRouteMapNokSegments);

  constructor(private store: Store<AppState>) {
  }
}
