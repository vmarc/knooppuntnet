import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorAdminToggleComponent } from '../components/monitor-admin-toggle.component';
import { MonitorPageMenuComponent } from '../components/monitor-page-menu.component';
import { MonitorGroupTableComponent } from './monitor-group-table.component';
import { MonitorGroupsPageService } from './monitor-groups-page.service';

@Component({
  selector: 'kpn-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li i18n="@@breadcrumb.monitor">Monitor</li>
      </ul>

      <kpn-page-header>
        <ng-container i18n="@@monitor.groups.title">Monitor</ng-container>
      </kpn-page-header>

      <kpn-monitor-page-menu pageName="groups" />
      <kpn-error />

      @if (service.state(); as state) {
        @if (state.response; as response) {
          @if (response.result; as page) {
            <div class="header">
              <div id="routes-in-groups" i18n="@@monitor.groups.routes-in-groups">
                {{ page.routeCount }} routes in {{ page.groups.length }} groups
              </div>
              <kpn-monitor-admin-toggle />
            </div>
            @if (page.groups.length > 0) {
              <kpn-monitor-group-table [admin]="service.admin()" [groups]="page.groups" />
            } @else {
              <div id="no-groups" i18n="@@monitor.groups.no-groups">No route groups</div>
            }

            @if (service.admin()) {
              <div class="kpn-spacer-above">
                <button
                  mat-stroked-button
                  routerLink="/monitor/admin/groups/add"
                  i18n="@@monitor.groups.action.add"
                >
                  Add group
                </button>
              </div>
            }
          }
        }
      }
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
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
  providers: [MonitorGroupsPageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MatButtonModule,
    MonitorAdminToggleComponent,
    MonitorGroupTableComponent,
    MonitorPageMenuComponent,
    PageComponent,
    RouterLink,
    SidebarComponent,
    PageHeaderComponent,
  ],
})
export class MonitorGroupsPageComponent {
  protected readonly service = inject(MonitorGroupsPageService);
}
