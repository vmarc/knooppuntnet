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
import { MonitorRoutePropertiesComponent } from '../components/monitor-route-properties.component';
import { MonitorRouteAddPageService } from './monitor-route-add-page.service';

@Component({
  selector: 'kpn-monitor-route-add-page',
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
          {{ state.groupDescription }}
        </kpn-page-header>

        <h2>{{ subtitle }}</h2>

        <kpn-error />

        <kpn-monitor-route-form
          mode="add"
          [groupName]="state.groupName"
          [initialProperties]="{ groupName: state.groupName }"
        />

        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  providers: [MonitorRouteAddPageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MonitorRoutePropertiesComponent,
    PageComponent,
    RouterLink,
    SidebarComponent,
    MonitorRouteFormComponent,
    PageHeaderComponent,
  ],
})
export class MonitorRouteAddPageComponent {
  protected readonly subtitle = $localize`:@@monitor.route.add.title:Add route`;
  protected readonly service = inject(MonitorRouteAddPageService);
  protected readonly pageTitle = computed(() => {
    const groupName = this.service.state().groupName;
    const monitor = MonitorTranslations.get('monitor');
    return `${this.subtitle} | ${groupName} | ${monitor}`;
  });
}
