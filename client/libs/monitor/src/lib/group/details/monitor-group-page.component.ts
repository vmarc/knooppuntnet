import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { actionMonitorGroupPageDestroy } from '../../store/monitor.actions';
import { actionMonitorGroupPageInit } from '../../store/monitor.actions';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';
import { selectMonitorAdmin } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li i18n="@@breadcrumb.monitor.group">Group</li>
    </ul>

    <h1 class="title" *ngIf="groupName$ | async as groupName">
      <span class="kpn-label">{{ groupName }}</span>
      <span>{{ groupDescription$ | async }}</span>
    </h1>

    <kpn-monitor-group-page-menu
      pageName="routes"
      [groupName]="groupName$ | async"
    />

    <kpn-monitor-admin-toggle />

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="hasRoutes$ | async; else noRoutes">
        <kpn-monitor-group-route-table
          [groupName]="groupName$ | async"
          [routes]="response.result.routes"
        />
      </div>
      <ng-template #noRoutes>
        <div i18n="@@monitor.group.no-routes">No routes in group</div>
      </ng-template>
      <div *ngIf="admin$ | async" class="kpn-form-buttons">
        <button
          mat-stroked-button
          [routerLink]="addRouteLink$ | async"
          type="button"
          i18n="@@monitor.group.action.add-route"
        >
          Add route
        </button>
      </div>
    </div>
  `,
})
export class MonitorGroupPageComponent implements OnInit, OnDestroy {
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly addRouteLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/admin/groups/${groupName}/routes/add`)
  );
  readonly admin$ = this.store.select(selectMonitorAdmin);
  readonly response$ = this.store.select(selectMonitorGroupPage);

  readonly hasRoutes$: Observable<boolean> = this.response$.pipe(
    map((response) => response?.result?.routes.length > 0)
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupPageDestroy());
  }
}
