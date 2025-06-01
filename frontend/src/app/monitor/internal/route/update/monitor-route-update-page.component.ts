import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteFormComponent } from '../components/monitor-route-form.component';
import { MonitorRouteUpdatePageService } from './monitor-route-update-page.service';

@Component({
  selector: 'ui-monitor-route-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <ui-page>
        <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
        <ui-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.routeName }}</span>
          <span>{{ state.routeDescription }}</span>
        </ui-page-header>

        <h2>{{ subtitle }}</h2>

        <ui-error />

        @if (state.response; as response) {
          <ui-monitor-route-form
            mode="update"
            [groupName]="state.groupName"
            [initialProperties]="response.result.properties"
            [routeGroups]="response.result.groups"
          />
        }
      </ui-page>
    }
  `,
  providers: [MonitorRouteUpdatePageService, NavService],
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    MonitorRouteFormComponent,
    PageComponent,
    PageHeaderComponent,
  ],
})
export class MonitorRouteUpdatePageComponent {
  protected readonly subtitle = $localize`:@@monitor.route.update.title:Update route`;
  protected readonly service = inject(MonitorRouteUpdatePageService);
  protected readonly pageTitle = computed(() => {
    const state = this.service.state();
    const monitor = MonitorTranslations.get('monitor');
    return `${this.subtitle} | ${state.routeName} | ${state.groupName} | ${monitor}`;
  });
  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    const groupLink = this.service.state().groupLink;
    const groupName = this.service.state().groupName;
    return [
      Breadcrumbs.home,
      Breadcrumbs.monitor,
      { routerLink: groupLink, label: groupName },
      { label: Breadcrumbs.monitorRouteLabel },
    ];
  });
}
