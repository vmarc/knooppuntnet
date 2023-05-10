import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { selectRouteParam } from '@app/core';
import { Store } from '@ngrx/store';
import { actionMonitorRouteDeletePageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteDeletePageInit } from '../../store/monitor.actions';
import { actionMonitorRouteDelete } from '../../store/monitor.actions';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink()">{{ groupName() }}</a>
      </li>
      <li i18n="@@breadcrumb.monitor.route">Route</li>
    </ul>

    <h1>
      <span class="kpn-label">{{ routeName() }}</span>
      <span>{{ routeDescription() }}</span>
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
        <a [routerLink]="groupLink()" i18n="@@action.cancel">Cancel</a>
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
  standalone: true,
  imports: [
    RouterLink,
    ErrorComponent,
    MatIconModule,
    MatButtonModule,
    AsyncPipe,
  ],
})
export class MonitorRouteDeletePageComponent implements OnInit, OnDestroy {
  readonly groupName = this.store.selectSignal(selectRouteParam('groupName'));
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName}`);
  readonly routeName = this.store.selectSignal(selectRouteParam('routeName'));
  readonly routeDescription = this.store.selectSignal(
    selectMonitorRouteDescription
  );

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
