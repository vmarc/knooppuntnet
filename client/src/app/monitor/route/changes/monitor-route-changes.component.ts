import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {Store} from '@ngrx/store';
import {AppState} from '../../../core/core.state';
import {actionPreferencesImpact} from '../../../core/preferences/preferences.actions';
import {selectPreferencesImpact} from '../../../core/preferences/preferences.selectors';
import {selectMonitorRouteChangesFiltered} from '../../store/monitor.selectors';
import {selectMonitorRouteChanges} from '../../store/monitor.selectors';
import {selectMonitorRouteId} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-monitor-route-page-header pageName="changes" [routeId]="routeId$ | async"></kpn-monitor-route-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route not found
      </div>
      <div *ngIf="response.result">

        <mat-slide-toggle [checked]="impact$ | async" (change)="impactChanged($event)">Impact</mat-slide-toggle>

        <kpn-items>
          <kpn-item *ngFor="let changeSet of changes$ | async; let i=index" [index]="i">

            <div class="change-set">

              <kpn-monitor-route-change-header [changeSet]="changeSet"></kpn-monitor-route-change-header>

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
export class MonitorRouteChangesComponent {

  readonly routeId$ = this.store.select(selectMonitorRouteId);
  readonly response$ = this.store.select(selectMonitorRouteChanges);
  readonly impact$ = this.store.select(selectPreferencesImpact);
  readonly changes$ = this.store.select(selectMonitorRouteChangesFiltered);

  constructor(private store: Store<AppState>) {
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({impact: event.checked}));
  }

}
