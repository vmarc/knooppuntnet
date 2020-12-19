import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {RouteGroupsPage} from '@api/common/monitor/route-groups-page';
import {ApiResponse} from '@api/custom/api-response';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';
import {AppState} from '../../core/core.state';
import {selectMonitorRouteGroups} from '../store/monitor.selectors';
import {selectMonitorAdmin} from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>Monitor</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-monitor-page-menu pageName="groups"></kpn-monitor-page-menu>

    <kpn-monitor-admin-toggle></kpn-monitor-admin-toggle>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result">
        No route groups
      </div>
      <div *ngIf="response.result">
        <kpn-monitor-group-table [groups]="response.result.groups"></kpn-monitor-group-table>
      </div>
    </div>

    <div *ngIf="admin$ | async" class="add-group-action">
      <button mat-stroked-button routerLink="/monitor/admin/groups/add">Add group</button>
    </div>
  `,
  styles: [`
    .add-group-action {
      margin-top: 3em;
    }
  `]
})
export class MonitorGroupsPageComponent {

  readonly admin$ = this.store.select(selectMonitorAdmin);

  readonly response$: Observable<ApiResponse<RouteGroupsPage>> = this.store.pipe(
    select(selectMonitorRouteGroups),
    filter(r => r != null)
  );

  constructor(private store: Store<AppState>) {
  }

}
