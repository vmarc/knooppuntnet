import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { selectRouteParam } from '@app/core';
import { Store } from '@ngrx/store';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteUpdatePageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteUpdatePageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';
import { selectMonitorRouteUpdatePage } from '../../store/monitor.selectors';
import { MonitorRoutePropertiesComponent } from '../components/monitor-route-properties.component';

@Component({
  selector: 'kpn-monitor-route-update-page',
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
      <span> {{ routeDescription() }}</span
      >&nbsp;
    </h1>

    <h2 i18n="@@monitor.route.update.title">Update route</h2>

    <kpn-error />

    <div *ngIf="response()?.result as page">
      <kpn-monitor-route-properties
        mode="update"
        [groupName]="groupName()"
        [initialProperties]="page.properties"
        [routeGroups]="page.groups"
      />
    </div>
  `,
  standalone: true,
  imports: [
    RouterLink,
    ErrorComponent,
    NgIf,
    MonitorRoutePropertiesComponent,
    AsyncPipe,
  ],
})
export class MonitorRouteUpdatePageComponent implements OnInit, OnDestroy {
  readonly response = this.store.selectSignal(selectMonitorRouteUpdatePage);
  readonly groupName = this.store.selectSignal(selectRouteParam('groupName'));
  readonly routeName = this.store.selectSignal(selectRouteParam('routeName'));
  readonly routeDescription = this.store.selectSignal(
    selectMonitorRouteDescription
  );
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName()}`);

  constructor(private monitorService: MonitorService, private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteUpdatePageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteUpdatePageDestroy());
  }
}
