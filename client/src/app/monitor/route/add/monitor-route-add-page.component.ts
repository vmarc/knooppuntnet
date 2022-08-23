import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteAddPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteAddPage } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink$ | async">{{ groupDescription$ | async }}</a>
      </li>
      <li>Route</li>
    </ul>

    <h1>
      {{ groupDescription$ | async }}
    </h1>

    <h2>Add route</h2>

    <kpn-monitor-route-properties mode="add"></kpn-monitor-route-properties>
  `,
})
export class MonitorRouteAddPageComponent implements OnInit {
  readonly response$ = this.store.select(selectMonitorRouteAddPage);
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteAddPageInit());
  }
}
