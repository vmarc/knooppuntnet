import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';
import { selectMonitorRouteId } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorRouteName } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink$ | async">{{ groupName$ | async }}</a>
      </li>
      <li>Route</li>
    </ul>

    <h1 class="title" *ngIf="routeName$ | async as routeName">
      <span class="kpn-label">{{ routeName }}</span>
      <span>{{ routeDescription$ | async }}</span>
    </h1>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="routeDetailLink$ | async"
        [active]="pageName === 'details'"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="routeMapLink$ | async"
        [active]="pageName === 'map'"
      >
        Map
      </kpn-page-menu-option>

      <!--  <kpn-page-menu-option-->
      <!--    [link]="routeReferenceLink$ | async"-->
      <!--    [active]="pageName === 'reference'"-->
      <!--  >-->
      <!--    Reference-->
      <!--  </kpn-page-menu-option>-->

      <!--  <kpn-page-menu-option-->
      <!--    [link]="routeChangesLink$ | async"-->
      <!--    [active]="pageName === 'changes'"-->
      <!--  >-->
      <!--    Changes-->
      <!--  </kpn-page-menu-option>-->
    </kpn-page-menu>

    <kpn-error></kpn-error>
  `,
  styles: [
    `
      .title {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    `,
  ],
})
export class MonitorRoutePageHeaderComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly routeId$ = this.store.select(selectMonitorRouteId);
  readonly routeName$ = this.store.select(selectMonitorRouteName);
  readonly routeDescription$ = this.store.select(selectMonitorRouteDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  readonly routeDetailLink$ = combineLatest([
    this.store.select(selectMonitorGroupName),
    this.store.select(selectMonitorRouteName),
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}`
    )
  );

  readonly routeMapLink$ = combineLatest([
    this.store.select(selectMonitorGroupName),
    this.store.select(selectMonitorRouteName),
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}/map`
    )
  );

  readonly routeReferenceLink$ = combineLatest([
    this.store.select(selectMonitorGroupName),
    this.store.select(selectMonitorRouteName),
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}/reference`
    )
  );

  readonly routeChangesLink$ = combineLatest([
    this.store.select(selectMonitorGroupName),
    this.store.select(selectMonitorRouteName),
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}/changes`
    )
  );

  constructor(private store: Store<AppState>) {}
}
