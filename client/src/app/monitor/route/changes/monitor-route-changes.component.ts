import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {Store} from '@ngrx/store';
import {AppState} from '../../../core/core.state';
import {actionPreferencesImpact} from '../../../core/preferences/preferences.actions';
import {selectPreferencesImpact} from '../../../core/preferences/preferences.selectors';
import {actionMonitorRouteChangesPageInit} from '../../store/monitor.actions';
import {selectMonitorRouteChangesPage} from '../../store/monitor.selectors';
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
        <kpn-monitor-changes [changes]="response.result.changes"></kpn-monitor-changes>
      </div>
    </div>
  `
})
export class MonitorRouteChangesComponent implements OnInit {

  readonly routeId$ = this.store.select(selectMonitorRouteId);
  readonly response$ = this.store.select(selectMonitorRouteChangesPage);
  readonly impact$ = this.store.select(selectPreferencesImpact);

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteChangesPageInit());
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({impact: event.checked}));
  }

}
