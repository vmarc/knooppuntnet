import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { MonitorGroupPage } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupPageService } from './monitor-group-page.service';
import { MonitorGroupRouteTableComponent } from './monitor-group-route-table.component';

@Component({
  selector: 'kpn-monitor-group-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li i18n="@@breadcrumb.monitor.group">Group</li>
    </ul>

    <h1 class="title" *ngIf="service.groupName() as groupName">
      <span class="kpn-label">{{ groupName }}</span>
      <span>{{ service.groupDescription() }}</span>
    </h1>

    <kpn-monitor-group-page-menu
      pageName="routes"
      [groupName]="service.groupName()"
    />

    <kpn-monitor-admin-toggle />

    <div *ngIf="service.apiResponse() as response" class="kpn-form">
      <div *ngIf="response.result?.routes.length > 0; else noRoutes">
        <kpn-monitor-group-route-table
          [admin]="service.admin()"
          [groupName]="response.result.groupName"
          [routes]="response.result.routes"
        />
        <div *ngIf="service.admin()" class="kpn-form-buttons">
          <button
            mat-stroked-button
            id="add-route"
            [routerLink]="addRouteLink(response.result)"
            type="button"
            i18n="@@monitor.group.action.add-route"
          >
            Add route
          </button>
        </div>
      </div>
      <ng-template #noRoutes>
        <div id="no-routes" i18n="@@monitor.group.no-routes">
          No routes in group
        </div>
      </ng-template>
    </div>
  `,
  providers: [MonitorGroupPageService, NavService],
  standalone: true,
  imports: [
    AsyncPipe,
    MatButtonModule,
    MonitorAdminToggleComponent,
    MonitorGroupPageMenuComponent,
    MonitorGroupRouteTableComponent,
    NgIf,
    RouterLink,
  ],
})
export class MonitorGroupPageComponent implements OnInit {
  constructor(protected service: MonitorGroupPageService) {}

  ngOnInit(): void {
    this.service.init();
  }

  addRouteLink(page: MonitorGroupPage): string {
    return `/monitor/admin/groups/${page.groupName}/routes/add`;
  }
}
