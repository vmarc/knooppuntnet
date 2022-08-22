import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteDeletePageInit } from '../../store/monitor.actions';
import { actionMonitorRouteDelete } from '../../store/monitor.actions';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';
import { selectMonitorRouteName } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-delete-page',
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

    <h2>Delete</h2>

    <div class="kpn-form">
      <p>Remove this route from the monitor.</p>

      <p class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <span>Attention: all history will be lost!</span>
      </p>

      <div class="kpn-form-buttons">
        <button mat-stroked-button (click)="delete()">
          <span class="delete-button">Delete Route</span>
        </button>
        <a [routerLink]="groupLink$ | async">Cancel</a>
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
export class MonitorRouteDeletePageComponent implements OnInit {
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );
  readonly routeName$ = this.store.select(selectMonitorRouteName);
  readonly routeDescription$ = this.store.select(selectMonitorRouteDescription);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteDeletePageInit());
  }

  delete(): void {
    this.store.dispatch(actionMonitorRouteDelete());
  }
}
