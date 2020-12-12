import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteChanges} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteId} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-long-distance-route-page-header pageName="changes" [routeId]="routeId$ | async"></kpn-long-distance-route-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route not found
      </div>
      <div *ngIf="response.result">

        <kpn-items>
          <kpn-item *ngFor="let changeSet of response.result.changes; let i=index" [index]="i">

            <div class="change-set">

              <kpn-long-distance-route-change-header [changeSet]="changeSet"></kpn-long-distance-route-change-header>

              <div>
                <p>
                  Reference: {{changeSet.gpxFilename}}
                </p>
                <table>
                  <tr>
                    <td>GPX</td>
                    <td>{{changeSet.gpxDistance}}km</td>
                  </tr>
                  <tr>
                    <td>OSM</td>
                    <td>{{changeSet.osmDistance}}km</td>
                  </tr>
                </table>
                <p>
                  ways={{changeSet.wayCount}},
                  added={{changeSet.waysAdded}},
                  removed={{changeSet.waysRemoved}},
                  updated={{changeSet.waysUpdated}}
                </p>
                <p *ngIf="changeSet.routeSegmentCount !== 1">
                  Not OK: {{changeSet.routeSegmentCount}} route segments
                </p>
                <p *ngIf="changeSet.routeSegmentCount === 1">
                  OK: 1 route segment
                </p>
                <p *ngIf="changeSet.resolvedNokSegmentCount > 0" class="kpn-line">
                  <span>Resolved deviations: {{changeSet.resolvedNokSegmentCount}}</span>
                  <kpn-icon-happy></kpn-icon-happy>
                </p>
                <p *ngIf="changeSet.newNokSegmentCount > 0" class="kpn-line">
                  <span>New deviations: {{changeSet.newNokSegmentCount}}</span>
                  <kpn-icon-investigate></kpn-icon-investigate>
                </p>
              </div>
            </div>
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `
})
export class LongDistanceRouteChangesComponent {

  routeId$ = this.store.select(selectLongDistanceRouteId);
  response$ = this.store.select(selectLongDistanceRouteChanges);

  constructor(private store: Store<AppState>) {
  }

}
