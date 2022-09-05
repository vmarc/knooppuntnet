import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteAddPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteAddPage } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink$ | async">{{ groupName$ | async }}</a>
      </li>
      <li i18n="@@breadcrumb.monitor.route">Route</li>
    </ul>

    <h1>{{ groupDescription$ | async }}&nbsp;</h1>

    <h2 i18n="@@monitor.route.add.title">Add route</h2>

    <kpn-error></kpn-error>

    <kpn-monitor-route-properties
      mode="add"
      [groupName]="groupName$ | async"
    ></kpn-monitor-route-properties>
  `,
})
export class MonitorRouteAddPageComponent implements OnInit {
  readonly response$ = this.store.select(selectMonitorRouteAddPage);
  readonly groupName$ = this.store.select(selectRouteParam('groupName'));
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteAddPageInit());
  }
}
