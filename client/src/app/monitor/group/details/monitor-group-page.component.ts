import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {of} from 'rxjs';
import {AppState} from '../../../core/core.state';
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
    <kpn-monitor-admin-toggle></kpn-monitor-admin-toggle>
    <kpn-monitor-group-route-table></kpn-monitor-group-route-table>

    <div *ngIf="admin$ | async" class="add-group-action">
      <button mat-stroked-button [routerLink]="addRouteLink$ | async" type="button">
        Add route
      </button>
    </div>
  `,
  styles: [`
    .add-group-action {
      padding-top: 3em;
    }
  `]
})
export class MonitorGroupPageComponent {

  readonly groupName$ = of('group-1');
  readonly groupDescription$ = of('Group One');
  readonly addRouteLink$ = of('/monitor/admin/groups/group-1/routes/add');

  readonly admin$ = this.store.select(selectMonitorAdmin);

  constructor(private store: Store<AppState>) {
  }

}
