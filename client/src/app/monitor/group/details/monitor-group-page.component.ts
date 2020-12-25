import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MonitorGroupPage} from '@api/common/monitor/monitor-group-page';
import {ApiResponse} from '@api/custom/api-response';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {filter} from 'rxjs/operators';
import {AppState} from '../../../core/core.state';
import {actionMonitorGroupPageInit} from '../../store/monitor.actions';
import {selectMonitorGroupDescription} from '../../store/monitor.selectors';
import {selectMonitorGroupName} from '../../store/monitor.selectors';
import {selectMonitorGroupPage} from '../../store/monitor.selectors';
import {selectMonitorAdmin} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>
      {{groupDescription$ | async}}
    </h1>

    <kpn-monitor-group-page-menu pageName="routes" [groupName]="groupName$ | async"></kpn-monitor-group-page-menu>

    <div *ngIf="response$ | async as response">
      <p *ngIf="!response.result || response.result.routes.length === 0">
        No route groups
      </p>
      <div *ngIf="response.result && response.result.routes.length > 0">
        <kpn-monitor-admin-toggle></kpn-monitor-admin-toggle>
        <kpn-monitor-group-route-table
          [groupName]="groupName$ | async"
          [routes]="response.result.routes">
        </kpn-monitor-group-route-table>

        <div *ngIf="admin$ | async" class="add-route-action">
          <button mat-stroked-button [routerLink]="addRouteLink$ | async" type="button">
            Add route
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .add-route-action {
      padding-top: 3em;
    }
  `]
})
export class MonitorGroupPageComponent implements OnInit {

  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly addRouteLink$ = this.groupName$.pipe(map(groupName => `/monitor/admin/groups/${groupName}/routes/add`));
  readonly admin$ = this.store.select(selectMonitorAdmin);

  readonly response$: Observable<ApiResponse<MonitorGroupPage>> = this.store.pipe(
    select(selectMonitorGroupPage),
    filter(r => r != null)
  );

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupPageInit());
  }
}
