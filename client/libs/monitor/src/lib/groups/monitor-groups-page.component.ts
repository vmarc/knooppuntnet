import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { MonitorAdminToggleComponent } from '../components/monitor-admin-toggle.component';
import { MonitorPageMenuComponent } from '../components/monitor-page-menu.component';
import { MonitorGroupTableComponent } from './monitor-group-table.component';
import { MonitorGroupsPageService } from './monitor-groups-page.service';

@Component({
  selector: 'kpn-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="service.state() as state">
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li i18n="@@breadcrumb.monitor">Monitor</li>
      </ul>

      <h1 i18n="@@monitor.groups.title">Monitor</h1>

      <kpn-monitor-page-menu pageName="groups" />
      <kpn-error />

      <ng-container *ngIf="state.response as response">
        <ng-container *ngIf="response.result as page">
          <div class="header">
            <div id="routes-in-groups" i18n="@@monitor.groups.routes-in-groups">
              {{ page.routeCount }} routes in {{ page.groups.length }} groups
            </div>
            <kpn-monitor-admin-toggle />
          </div>
          <div *ngIf="page.groups.length > 0; else noGroups">
            <kpn-monitor-group-table
              [admin]="service.admin()"
              [groups]="page.groups"
            />
          </div>
          <ng-template #noGroups>
            <div id="no-groups" i18n="@@monitor.groups.no-groups">
              No route groups
            </div>
          </ng-template>
          <div *ngIf="service.admin()" class="kpn-spacer-above">
            <button
              mat-stroked-button
              routerLink="/monitor/admin/groups/add"
              i18n="@@monitor.groups.action.add"
            >
              Add group
            </button>
          </div>
        </ng-container>
      </ng-container>
    </ng-container>
  `,
  styles: [
    `
      .header {
        display: flex;
        align-items: center;
        padding-top: 1em;
        padding-bottom: 2em;
      }

      kpn-monitor-admin-toggle {
        flex-grow: 1;
      }
    `,
  ],
  standalone: true,
  imports: [
    ErrorComponent,
    MatButtonModule,
    MonitorAdminToggleComponent,
    MonitorGroupTableComponent,
    MonitorPageMenuComponent,
    NgIf,
    RouterLink,
  ],
})
export class MonitorGroupsPageComponent {
  constructor(protected service: MonitorGroupsPageService) {}
}
