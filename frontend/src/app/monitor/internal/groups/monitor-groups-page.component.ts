import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorGroupsPageGroup } from '@api/common/monitor/monitor-groups-page-group';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorAdminToggleComponent } from '../components/monitor-admin-toggle.component';
import { MonitorGroupTableComponent } from './monitor-group-table.component';
import { MonitorGroupsPageService } from './monitor-groups-page.service';

@Component({
  selector: 'ui-monitor-groups',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header>
        <ng-container i18n="@@monitor.groups.title">Monitor</ng-container>
      </ui-page-header>

      <ui-error />

      @if (service.pageState(); as state) {
        @if (state.response; as response) {
          @if (response.result; as page) {
            <div class="header">
              <div id="routes-in-groups" i18n="@@monitor.groups.routes-in-groups">
                {{ page.routeCount }} routes in {{ page.groups.length }} groups
              </div>
              <ui-monitor-admin-toggle />
            </div>
            @if (page.groups.length > 0) {
              <ui-monitor-group-table
                [admin]="service.admin()"
                [groups]="page.groups"
                (selectGroup)="selectGroup($event)"
              />
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
    BreadcrumbComponent,
    ErrorComponent,
    MonitorAdminToggleComponent,
    MonitorGroupTableComponent,
    NzButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorGroupsPageComponent {
  protected readonly service = inject(MonitorGroupsPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    { label: Breadcrumbs.monitorLabel },
  ];

  selectGroup(group: MonitorGroupsPageGroup): void {
    this.service.selectGroup(group);
  }
}
