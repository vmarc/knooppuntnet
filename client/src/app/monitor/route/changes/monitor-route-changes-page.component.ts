import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {Store} from '@ngrx/store';
import {AppState} from '../../../core/core.state';
import {actionPreferencesImpact} from '../../../core/preferences/preferences.actions';
import {selectPreferencesImpact} from '../../../core/preferences/preferences.selectors';
import {actionMonitorRouteChangesPageIndex} from '../../store/monitor.actions';
import {actionMonitorRouteChangesPageInit} from '../../store/monitor.actions';
import {selectMonitorRouteChangesPage} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-monitor-route-page-header pageName="changes"></kpn-monitor-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route not found
      </div>
      <div *ngIf="response.result" class="kpn-spacer-above">

        <mat-slide-toggle [checked]="impact$ | async" (change)="impactChanged($event)">Impact</mat-slide-toggle>

        <kpn-paginator
          (page)="pageChanged($event)"
          [pageIndex]="response.result.pageIndex"
          [length]="response.result.totalChangeCount"
          [showPageSizeSelection]="true">
        </kpn-paginator>

        <kpn-monitor-changes
          [pageIndex]="response.result.pageIndex"
          [itemsPerPage]="response.result.itemsPerPage"
          [changes]="response.result.changes">
        </kpn-monitor-changes>

      </div>
    </div>
  `
})
export class MonitorRouteChangesPageComponent implements OnInit {

  readonly response$ = this.store.select(selectMonitorRouteChangesPage);
  readonly impact$ = this.store.select(selectPreferencesImpact);

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteChangesPageInit());
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({impact: event.checked}));
    this.store.dispatch(actionMonitorRouteChangesPageInit());
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this.store.dispatch(actionMonitorRouteChangesPageIndex({pageIndex: event.pageIndex}));
  }
}
