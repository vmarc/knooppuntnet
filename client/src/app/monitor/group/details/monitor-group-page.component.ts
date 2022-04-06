import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { ApiResponse } from '@api/custom/api-response';
import { select } from '@ngrx/store';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorGroupPageInit } from '../../store/monitor.actions';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';
import { selectMonitorAdmin } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1 class="title" *ngIf="groupName$ | async as groupName">
      <span class="kpn-label">{{ groupName }}</span>
      <span>{{ groupDescription$ | async }}</span>
    </h1>

    <kpn-monitor-group-page-menu
      pageName="routes"
      [groupName]="groupName$ | async"
    ></kpn-monitor-group-page-menu>

    <kpn-monitor-admin-toggle></kpn-monitor-admin-toggle>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="hasRoutes$ | async; else noRoutes">
        <kpn-monitor-group-route-table
          [groupName]="groupName$ | async"
          [routes]="response.result.routes"
        >
        </kpn-monitor-group-route-table>
      </div>
      <ng-template #noRoutes>
        <div>No routes in group</div>
      </ng-template>
      <div *ngIf="admin$ | async" class="kpn-form-buttons">
        <button
          mat-stroked-button
          [routerLink]="addRouteLink$ | async"
          type="button"
        >
          Add route
        </button>
      </div>
    </div>
  `,
  styles: [``],
})
export class MonitorGroupPageComponent implements OnInit {
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly addRouteLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/admin/groups/${groupName}/routes/add`)
  );
  readonly admin$ = this.store.select(selectMonitorAdmin);

  readonly response$: Observable<ApiResponse<MonitorGroupPage>> =
    this.store.pipe(
      select(selectMonitorGroupPage),
      filter((r) => r != null)
    );

  readonly hasRoutes$: Observable<boolean> = this.response$.pipe(
    map((response) => response.result?.routes.length > 0)
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupPageInit());
  }
}
