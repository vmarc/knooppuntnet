import { NgIf } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { MonitorRoutePropertiesComponent } from '../components/monitor-route-properties.component';
import { MonitorRouteUpdatePageService } from './monitor-route-update-page.service';

@Component({
  selector: 'kpn-monitor-route-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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

      <h1>
        <span class="kpn-label">{{ service.routeName() }}</span>
        <span> {{ service.routeDescription() }}</span
        >&nbsp;
      </h1>

      <h2 i18n="@@monitor.route.update.title">Update route</h2>

      <kpn-error />

      <div *ngIf="service.apiResponse() as response">
        <kpn-monitor-route-properties
          mode="update"
          [groupName]="groupName"
          [initialProperties]="response.result.properties"
          [routeGroups]="response.result.groups"
        />
      </div>
    </ng-container>
  `,
  providers: [MonitorRouteUpdatePageService, NavService],
  standalone: true,
  imports: [ErrorComponent, MonitorRoutePropertiesComponent, NgIf, RouterLink],
})
export class MonitorRouteUpdatePageComponent implements OnInit {
  constructor(protected service: MonitorRouteUpdatePageService) {}

  ngOnInit(): void {
    this.service.init();
  }

  groupLink(groupName: string) {
    return `/monitor/groups/${groupName}`;
  }
}
