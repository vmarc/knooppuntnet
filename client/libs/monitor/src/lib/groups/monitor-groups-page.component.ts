import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { selectDefined } from '@app/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { MonitorAdminToggleComponent } from '../components/monitor-admin-toggle.component';
import { MonitorPageMenuComponent } from '../components/monitor-page-menu.component';
import { actionMonitorGroupsPageDestroy } from '../store/monitor.actions';
import { actionMonitorGroupsPageInit } from '../store/monitor.actions';
import { selectMonitorGroupsPage } from '../store/monitor.selectors';
import { selectMonitorAdmin } from '../store/monitor.selectors';
import { MonitorGroupTableComponent } from './monitor-group-table.component';

@Component({
  selector: 'kpn-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li i18n="@@breadcrumb.monitor">Monitor</li>
    </ul>

    <h1 i18n="@@monitor.groups.title">Monitor</h1>

    <kpn-monitor-page-menu pageName="groups" />
    <kpn-error />

    <div *ngIf="response$ | async as response">
      <div class="header">
        <div i18n="@@monitor.groups.routes-in-groups">
          {{ routeCount$ | async }} routes in {{ groupCount$ | async }} groups
        </div>
        <kpn-monitor-admin-toggle />
      </div>
      <div *ngIf="hasGroups$ | async; else noGroups">
        <kpn-monitor-group-table [groups]="response.result.groups" />
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
  standalone: true,
  imports: [
    RouterLink,
    MonitorPageMenuComponent,
    ErrorComponent,
    NgIf,
    MonitorAdminToggleComponent,
    MonitorGroupTableComponent,
    MatButtonModule,
    AsyncPipe,
  ],
})
export class MonitorGroupsPageComponent implements OnInit, OnDestroy {
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

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupsPageDestroy());
  }
}