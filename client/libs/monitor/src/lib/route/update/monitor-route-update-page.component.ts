import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorRouteFormComponent } from '../components/monitor-route-form.component';
import { MonitorRouteUpdatePageService } from './monitor-route-update-page.service';

@Component({
  selector: 'kpn-monitor-route-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page *ngIf="service.state() as state">
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </li>
        <li>
          <a [routerLink]="state.groupLink">{{ state.groupName }}</a>
        </li>
        <li i18n="@@breadcrumb.monitor.route">Route</li>
      </ul>

      <h1>
        <span class="kpn-label">{{ state.routeName }}</span>
        <span> {{ state.routeDescription }}</span
        >&nbsp;
      </h1>

      <h2 i18n="@@monitor.route.update.title">Update route</h2>

      <kpn-error />

      <div *ngIf="state.response as response">
        <kpn-monitor-route-form
          mode="update"
          [groupName]="state.groupName"
          [initialProperties]="response.result.properties"
          [routeGroups]="response.result.groups"
        />
      </div>

      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [MonitorRouteUpdatePageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MonitorRouteFormComponent,
    NgIf,
    PageComponent,
    RouterLink,
    SidebarComponent,
  ],
})
export class MonitorRouteUpdatePageComponent {
  constructor(protected service: MonitorRouteUpdatePageService) {}
}
