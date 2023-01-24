import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs';
import { selectRouteParam } from '../../../core/core.state';
import { selectDefined } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteUpdatePageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteUpdatePageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';
import { selectMonitorRouteUpdatePage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-update-page',
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

    <h1>
      <span class="kpn-label">{{ routeName$ | async }}</span>
      <span> {{ routeDescription$ | async }}</span
      >&nbsp;
    </h1>

    <h2 i18n="@@monitor.route.update.title">Update route</h2>

    <kpn-error/>

    <div *ngIf="response$ | async as response">
      <kpn-monitor-route-properties
        mode="update"
        [groupName]="groupName$ | async"
        [initialProperties]="response.result.properties"
        [routeGroups]="response.result.groups"
      />
    </div>
  `,
})
export class MonitorRouteUpdatePageComponent implements OnInit, OnDestroy {
  readonly response$ = selectDefined(this.store, selectMonitorRouteUpdatePage);
  readonly groupName$ = this.store.select(selectRouteParam('groupName'));
  readonly routeName$ = this.store.select(selectRouteParam('routeName'));
  readonly routeDescription$ = this.store.select(selectMonitorRouteDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  constructor(
    private monitorService: MonitorService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteUpdatePageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteUpdatePageDestroy());
  }
}
