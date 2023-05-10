import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { Store } from '@ngrx/store';
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

    <div *ngIf="responseSignal() as response">
      <div class="header">
        <div id="routes-in-groups" i18n="@@monitor.groups.routes-in-groups">
          {{ routeCount() }} routes in {{ groupCount() }} groups
        </div>
        <kpn-monitor-admin-toggle />
      </div>
      <div *ngIf="hasGroups(); else noGroups">
        <kpn-monitor-group-table [groups]="response.result.groups" />
      </div>
      <ng-template #noGroups>
        <div id="no-groups" i18n="@@monitor.groups.no-groups">
          No route groups
        </div>
      </ng-template>
      <div *ngIf="admin()" class="kpn-spacer-above">
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
  readonly admin = this.store.selectSignal(selectMonitorAdmin);
  readonly responseSignal = this.store.selectSignal(selectMonitorGroupsPage);
  readonly hasGroups = computed(
    () => this.responseSignal().result?.groups?.length > 0
  );
  readonly routeCount = computed(
    () => this.responseSignal().result?.routeCount
  );
  readonly groupCount = computed(
    () => this.responseSignal().result?.groups?.length
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupsPageDestroy());
  }
}
