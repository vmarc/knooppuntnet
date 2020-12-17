import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../core/core.state';
import {selectMonitorAdmin} from '../../core/monitor/monitor.selectors';

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

    <kpn-monitor-group-table></kpn-monitor-group-table>

    <div *ngIf="admin$ | async" class="add-group-action">
      <button mat-stroked-button [routerLink]="'/monitor/admin/groups/add'">Add group</button>
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

  constructor(private store: Store<AppState>) {
  }

}
