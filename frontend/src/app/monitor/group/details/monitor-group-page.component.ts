import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorAdminToggleComponent } from '../../components/monitor-admin-toggle.component';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupPageService } from './monitor-group-page.service';
import { MonitorGroupRouteTableComponent } from './monitor-group-route-table.component';

@Component({
  selector: 'kpn-monitor-group-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </li>
        <li i18n="@@breadcrumb.monitor.group">Group</li>
      </ul>
      @if (service.state(); as state) {
        <kpn-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.groupName }}</span>
          <span>{{ state.groupDescription }}</span>
        </kpn-page-header>

        <kpn-monitor-group-page-menu pageName="routes" [groupName]="state.groupName" />

        <kpn-monitor-admin-toggle />

        @if (state.response; as response) {
          <div class="kpn-form">
            @if (response.result; as page) {
              @if (page.routes.length > 0) {
                <kpn-monitor-group-route-table
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
                    mat-stroked-button
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
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [NavService, MonitorGroupPageService],
  standalone: true,
  imports: [
    MatButtonModule,
    MonitorAdminToggleComponent,
    MonitorGroupPageMenuComponent,
    MonitorGroupRouteTableComponent,
    PageComponent,
    RouterLink,
    SidebarComponent,
    PageHeaderComponent,
  ],
})
export class MonitorGroupPageComponent {
  protected readonly service = inject(MonitorGroupPageService);
  private groupName = computed(() => this.service.state().groupName);
  protected pageTitle = computed(() => {
    const monitor = MonitorTranslations.translate('monitor');
    return `${this.groupName()} | ${monitor}`;
  });
  protected addRouteLink = computed(() => {
    return `/monitor/admin/groups/${this.groupName()}/routes/add`;
  });
}
