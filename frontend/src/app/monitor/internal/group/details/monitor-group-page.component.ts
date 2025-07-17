import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorGroupBreadcrumbComponent } from '@app/monitor/internal/group/components/monitor-group-breadcrumb.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupPageService } from './monitor-group-page.service';
import { MonitorGroupRouteTableComponent } from './monitor-group-route-table.component';

@Component({
  selector: 'ui-monitor-group-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-monitor-group-breadcrumb />

      @if (service.pageState(); as state) {
        <ui-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.groupName }}</span>
          <span>{{ state.groupDescription }}</span>
        </ui-page-header>

        <ui-monitor-group-page-menu pageName="routes" [groupName]="state.groupName" />

        <ui-monitor-admin-toggle />

        @if (state.response; as response) {
          <div class="kpn-form">
            @if (response.result; as page) {
              @if (page.routes.length > 0) {
                <ui-monitor-group-route-table
                  [admin]="service.admin()"
                  [groupName]="page.groupName"
                  [routes]="page.routes"
                />
              } @else {
                <div id="no-routes" i18n="@@monitor.group.no-routes">No routes in group</div>
              }
              @if (service.admin()) {
                <div class="kpn-form-buttons">
                  <button
                    nz-button
                    nzType="primary"
                    id="add-route"
                    [routerLink]="addRouteLink()"
                    type="button"
                    i18n="@@monitor.group.action.add-route"
                  >
                    Add route
                  </button>
                </div>
              }
            }
          </div>
        }
      }
    </ui-page>
  `,
  providers: [NavService, MonitorGroupPageService],
  imports: [
    MonitorAdminToggleComponent,
    MonitorGroupBreadcrumbComponent,
    MonitorGroupPageMenuComponent,
    MonitorGroupRouteTableComponent,
    NzButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorGroupPageComponent {
  readonly service = inject(MonitorGroupPageService);
  private groupName = computed(() => this.service.pageState().groupName);
  protected pageTitle = computed(() => {
    const monitor = MonitorTranslations.get('monitor');
    return `${this.groupName()} | ${monitor}`;
  });
  protected addRouteLink = computed(() => {
    return `/monitor/admin/groups/${this.groupName()}/routes/add`;
  });
}
