import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteMap} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteId} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-long-distance-route-page-header pageName="map" [routeId]="routeId$ | async"></kpn-long-distance-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route not found
      </div>
      <div *ngIf="response.result">
        Not implemented yet
      </div>
    </div>
  `,
  styles: [`
  `]
})
export class LongDistanceRouteMapComponent {

  routeId$ = this.store.select(selectLongDistanceRouteId);
  response$ = this.store.select(selectLongDistanceRouteMap);

  constructor(private store: Store<AppState>) {
  }

}
