import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteMapOsmSegments} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-map-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segments">
      <p>
        Segments
      </p>
      <div *ngFor="let segment of segments$ | async">
        {{segment.id}}
        {{segment.meters | distance}}
      </div>
    </div>
  `,
  styles: [`
    .segments {
      padding: 15px;
      border-bottom: 1px solid lightgray;
    }
  `]
})
export class LongDistanceRouteMapSegmentsComponent {

  segments$ = this.store.select(selectLongDistanceRouteMapOsmSegments);

  constructor(private store: Store<AppState>) {
  }
}
