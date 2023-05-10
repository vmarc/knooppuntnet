import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { Store } from '@ngrx/store';
import { actionMonitorRouteAddPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteAddPageInit } from '../../store/monitor.actions';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { MonitorRoutePropertiesComponent } from '../components/monitor-route-properties.component';

@Component({
  selector: 'kpn-monitor-route-add-page',
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

    <h1>{{ groupDescription() }}&nbsp;</h1>

    <h2 i18n="@@monitor.route.add.title">Add route</h2>

    <kpn-error />

    <kpn-monitor-route-properties mode="add" [groupName]="groupName()" />
  `,
  standalone: true,
  imports: [
    RouterLink,
    ErrorComponent,
    MonitorRoutePropertiesComponent,
    AsyncPipe,
  ],
})
export class MonitorRouteAddPageComponent implements OnInit, OnDestroy {
  readonly groupName = this.store.selectSignal(selectMonitorGroupName);
  readonly groupDescription = this.store.selectSignal(
    selectMonitorGroupDescription
  );
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName()}`);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteAddPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteAddPageDestroy());
  }
}
