import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteFormComponent } from '../components/monitor-route-form.component';
import { MonitorRouteUpdatePageService } from './monitor-route-update-page.service';

@Component({
  selector: 'kpn-monitor-route-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <kpn-page>
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

        <kpn-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.routeName }}</span>
          <span>{{ state.routeDescription }}</span>
        </kpn-page-header>

        <h2>{{ subtitle }}</h2>

        <kpn-error />

        @if (state.response; as response) {
          <kpn-monitor-route-form
            mode="update"
            [groupName]="state.groupName"
            [initialProperties]="response.result.properties"
            [routeGroups]="response.result.groups"
          />
        }
        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  providers: [MonitorRouteUpdatePageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MonitorRouteFormComponent,
    PageComponent,
    RouterLink,
    SidebarComponent,
    PageHeaderComponent,
  ],
})
export class MonitorRouteUpdatePageComponent {
  protected readonly subtitle = $localize`:@@monitor.route.update.title:Update route`;
  protected readonly service = inject(MonitorRouteUpdatePageService);
  protected readonly pageTitle = computed(() => {
    const state = this.service.state();
    const monitor = MonitorTranslations.translate('monitor');
    return `${this.subtitle} | ${state.routeName} | ${state.groupName} | ${monitor}`;
  });
}
