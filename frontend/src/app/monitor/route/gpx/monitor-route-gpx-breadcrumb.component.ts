import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

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
  groupName = input.required<string>();
  routeName = input.required<string>();
  groupLink = input.required<string>();
  routeLink = input.required<string>();
}
