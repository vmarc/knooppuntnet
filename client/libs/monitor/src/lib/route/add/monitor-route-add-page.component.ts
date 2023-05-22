import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorRoutePropertiesComponent } from '../components/monitor-route-properties.component';
import { MonitorRouteAddPageService } from './monitor-route-add-page.service';

@Component({
  selector: 'kpn-monitor-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ng-container *ngIf="service.groupName() as groupName">
        <ul class="breadcrumb">
          <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
          <li>
            <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
          </li>
          <li>
            <a [routerLink]="groupLink(groupName)">{{ groupName }}</a>
          </li>
          <li i18n="@@breadcrumb.monitor.route">Route</li>
        </ul>

        <h1>{{ service.groupDescription() }}&nbsp;</h1>

        <h2 i18n="@@monitor.route.add.title">Add route</h2>

        <kpn-error />

        <kpn-monitor-route-properties mode="add" [groupName]="groupName" />
      </ng-container>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [MonitorRouteAddPageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MonitorRoutePropertiesComponent,
    NgIf,
    PageComponent,
    RouterLink,
    SidebarComponent,
  ],
})
export class MonitorRouteAddPageComponent {
  constructor(protected service: MonitorRouteAddPageService) {}

  groupLink(groupName: string) {
    return `/monitor/groups/${groupName}`;
  }
}
