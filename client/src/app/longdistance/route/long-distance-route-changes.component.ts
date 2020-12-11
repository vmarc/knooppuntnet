import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {Util} from '../../components/shared/util';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteChanges} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteId} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-long-distance-route-page-header pageName="changes" [routeId]="routeId$ | async"></kpn-long-distance-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route not found
      </div>
      <div *ngIf="response.result">
        <pre>
{{util.json(response)}}
        </pre>
      </div>
    </div>
  `,
  styles: [`
  `]
})
export class LongDistanceRouteChangesComponent {

  util = Util;

  routeId$ = this.store.select(selectLongDistanceRouteId);
  response$ = this.store.select(selectLongDistanceRouteChanges);

  constructor(private store: Store<AppState>) {
  }

}
