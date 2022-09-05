import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectDefined } from '../../core/core.state';
import { AppState } from '../../core/core.state';
import { actionMonitorGroupsPageInit } from '../store/monitor.actions';
import { selectMonitorGroupsPage } from '../store/monitor.selectors';
import { selectMonitorAdmin } from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li i18n="@@breadcrumb.monitor">Monitor</li>
    </ul>

    <h1 i18n="@@monitor.groups.title">Monitor</h1>

    <kpn-monitor-page-menu pageName="groups"></kpn-monitor-page-menu>
    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <div class="header">
        <div i18n="@@monitor.groups.routes-in-groups">
          {{ routeCount$ | async }} routes in {{ groupCount$ | async }} groups
        </div>
        <kpn-monitor-admin-toggle></kpn-monitor-admin-toggle>
      </div>
      <div *ngIf="hasGroups$ | async; else noGroups">
        <kpn-monitor-group-table [groups]="response.result.groups">
        </kpn-monitor-group-table>
      </div>
      <ng-template #noGroups>
        <div i18n="@@monitor.groups.no-groups">No route groups</div>
      </ng-template>
      <div *ngIf="admin$ | async" class="kpn-spacer-above">
        <button
          mat-stroked-button
          routerLink="/monitor/admin/groups/add"
          i18n="@@monitor.groups.action.add"
        >
          Add group
        </button>
      </div>
    </div>
  `,
  styles: [
    `
      .header {
        display: flex;
        align-items: center;
        padding-top: 1em;
        padding-bottom: 2em;
      }

      kpn-monitor-admin-toggle {
        flex-grow: 1;
      }
    `,
  ],
})
export class MonitorGroupsPageComponent implements OnInit {
  readonly admin$ = this.store.select(selectMonitorAdmin);
  readonly response$ = selectDefined(this.store, selectMonitorGroupsPage);
  readonly hasGroups$ = this.response$.pipe(
    map((response) => response.result?.groups?.length > 0)
  );
  readonly routeCount$ = this.response$.pipe(
    map((response) => response.result?.routeCount)
  );
  readonly groupCount$ = this.response$.pipe(
    map((response) => response.result?.groups?.length)
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupsPageInit());
  }
}
