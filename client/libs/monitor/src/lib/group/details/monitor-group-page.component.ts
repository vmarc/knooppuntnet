import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { actionMonitorGroupPageDestroy } from '../../store/monitor.actions';
import { actionMonitorGroupPageInit } from '../../store/monitor.actions';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';
import { selectMonitorAdmin } from '../../store/monitor.selectors';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupRouteTableComponent } from './monitor-group-route-table.component';

@Component({
  selector: 'kpn-monitor-group-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li i18n="@@breadcrumb.monitor.group">Group</li>
    </ul>

    <h1 class="title" *ngIf="groupName()">
      <span class="kpn-label">{{ groupName() }}</span>
      <span>{{ groupDescription() }}</span>
    </h1>

    <kpn-monitor-group-page-menu pageName="routes" [groupName]="groupName()" />

    <kpn-monitor-admin-toggle />

    <div *ngIf="responseSignal() as response" class="kpn-form">
      <div *ngIf="hasRoutes(); else noRoutes">
        <kpn-monitor-group-route-table
          [groupName]="groupName()"
          [routes]="response.result.routes"
        />
      </div>
      <ng-template #noRoutes>
        <div id="no-routes" i18n="@@monitor.group.no-routes">
          No routes in group
        </div>
      </ng-template>
      <div *ngIf="admin()" class="kpn-form-buttons">
        <button
          mat-stroked-button
          id="add-route"
          [routerLink]="addRouteLink()"
          type="button"
          i18n="@@monitor.group.action.add-route"
        >
          Add route
        </button>
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    MonitorGroupPageMenuComponent,
    MonitorAdminToggleComponent,
    MonitorGroupRouteTableComponent,
    MatButtonModule,
    AsyncPipe,
  ],
})
export class MonitorGroupPageComponent implements OnInit, OnDestroy {
  readonly groupName = this.store.selectSignal(selectMonitorGroupName);
  readonly groupDescription = this.store.selectSignal(
    selectMonitorGroupDescription
  );
  readonly addRouteLink = computed(
    () => `/monitor/admin/groups/${this.groupName()}/routes/add`
  );
  readonly admin = this.store.selectSignal(selectMonitorAdmin);
  readonly responseSignal = this.store.selectSignal(selectMonitorGroupPage);
  readonly hasRoutes = computed(
    () => this.responseSignal()?.result?.routes.length > 0
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupPageDestroy());
  }
}
