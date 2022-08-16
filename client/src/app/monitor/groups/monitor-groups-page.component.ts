import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { actionMonitorGroupsPageInit } from '../store/monitor.actions';
import { selectMonitorGroupsPageHasGroups } from '../store/monitor.selectors';
import { selectMonitorGroupsPage } from '../store/monitor.selectors';
import { selectMonitorAdmin } from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>Monitor</li>
    </ul>

    <h1>Monitor</h1>

    <kpn-monitor-page-menu pageName="groups"></kpn-monitor-page-menu>
    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <kpn-monitor-admin-toggle></kpn-monitor-admin-toggle>
      <div *ngIf="hasGroups$ | async; else noGroups">
        <kpn-monitor-group-table [groups]="response.result.groups">
        </kpn-monitor-group-table>
      </div>
      <ng-template #noGroups>
        <div>No route groups</div>
      </ng-template>
      <div *ngIf="admin$ | async" class="kpn-spacer-above">
        <button mat-stroked-button routerLink="/monitor/admin/groups/add">
          Add group
        </button>
      </div>
    </div>
  `,
})
export class MonitorGroupsPageComponent implements OnInit {
  readonly admin$ = this.store.select(selectMonitorAdmin);
  readonly response$ = this.store.select(selectMonitorGroupsPage);
  readonly hasGroups$ = this.store.select(selectMonitorGroupsPageHasGroups);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupsPageInit());
  }
}
