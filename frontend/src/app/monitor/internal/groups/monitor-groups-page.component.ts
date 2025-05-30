import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorAdminToggleComponent } from '../components/monitor-admin-toggle.component';
import { MonitorGroupTableComponent } from './monitor-group-table.component';
import { MonitorGroupsPageService } from './monitor-groups-page.service';

@Component({
  selector: 'ui-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.monitor">Monitor</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <ui-page-header>
        <ng-container i18n="@@monitor.groups.title">Monitor</ng-container>
      </ui-page-header>

      <ui-error />

      @if (service.state(); as state) {
        @if (state.response; as response) {
          @if (response.result; as page) {
            <div class="header">
              <div id="routes-in-groups" i18n="@@monitor.groups.routes-in-groups">
                {{ page.routeCount }} routes in {{ page.groups.length }} groups
              </div>
              <ui-monitor-admin-toggle />
            </div>
            @if (page.groups.length > 0) {
              <ui-monitor-group-table [admin]="service.admin()" [groups]="page.groups" />
            } @else {
              <div id="no-groups" i18n="@@monitor.groups.no-groups">No route groups</div>
            }

            @if (service.admin()) {
              <div>
                <button
                  nz-button
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
    </ui-page>
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
  imports: [
    ErrorComponent,
    MatButtonModule,
    MonitorAdminToggleComponent,
    MonitorGroupTableComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    NzButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorGroupsPageComponent {
  readonly service = inject(MonitorGroupsPageService);
}
