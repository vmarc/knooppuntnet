import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { selectMonitorRouteName } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-gpx-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink()">{{ groupName() }}</a>
      </li>
      <li>
        <a [routerLink]="routeLink()">{{ routeName() }}</a>
      </li>
      <li i18n="@@breadcrumb.monitor.route.gpx">gpx</li>
    </ul>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class MonitorRouteGpxBreadcrumbComponent {
  readonly groupName = this.store.selectSignal(selectMonitorGroupName);
  readonly routeName = this.store.selectSignal(selectMonitorRouteName);
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName()}`);
  readonly routeLink = computed(
    () => `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`
  );

  constructor(private store: Store) {}
}
