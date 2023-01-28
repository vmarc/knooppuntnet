import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectRouteParam } from '../../../core/core.state';
import { actionMonitorRouteDeletePageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteDeletePageInit } from '../../store/monitor.actions';
import { actionMonitorRouteDelete } from '../../store/monitor.actions';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-delete-page',
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
      <span>{{ routeDescription$ | async }}</span>
    </h1>

    <h2 i18n="@@monitor.route.delete.title">Delete</h2>

    <kpn-error />

    <div class="kpn-form">
      <p i18n="@@monitor.route.delete.comment">
        Remove this route from the monitor.
      </p>

      <p class="kpn-line">
        <mat-icon svgIcon="warning" />
        <span i18n="@@monitor.route.delete.warning"
        >Attention: all history will be lost!</span
        >
      </p>

      <div class="kpn-form-buttons">
        <button mat-stroked-button (click)="delete()">
          <span class="delete-button" i18n="@@monitor.route.delete.action"
          >Delete Route</span
          >
        </button>
        <a [routerLink]="groupLink$ | async" i18n="@@action.cancel">Cancel</a>
      </div>
    </div>
  `,
  styles: [
    `
      .delete-button {
        color: red;
      }
    `,
  ],
})
export class MonitorRouteDeletePageComponent implements OnInit, OnDestroy {
  readonly groupName$ = this.store.select(selectRouteParam('groupName'));
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );
  readonly routeName$ = this.store.select(selectRouteParam('routeName'));
  readonly routeDescription$ = this.store.select(selectMonitorRouteDescription);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteDeletePageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteDeletePageDestroy());
  }

  delete(): void {
    this.store.dispatch(actionMonitorRouteDelete());
  }
}
