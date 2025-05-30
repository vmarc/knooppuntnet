import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';

@Component({
  selector: 'ui-monitor-route-gpx-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a [routerLink]="groupLink()">{{ groupName() }}</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a [routerLink]="routeLink()">{{ routeName() }}</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <span i18n="@@breadcrumb.monitor.route.gpx">gpx</span>
      </nz-breadcrumb-item>
    </nz-breadcrumb>
  `,
  imports: [RouterLink, NzBreadCrumbComponent, NzBreadCrumbItemComponent],
})
export class MonitorRouteGpxBreadcrumbComponent {
  groupName = input.required<string>();
  routeName = input.required<string>();
  groupLink = input.required<string>();
  routeLink = input.required<string>();
}
