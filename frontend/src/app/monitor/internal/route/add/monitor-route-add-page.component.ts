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
import { MonitorRouteAddPageService } from './monitor-route-add-page.service';

@Component({
  selector: 'ui-monitor-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <ui-page>
        <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
        <ui-page-header [pageTitle]="pageTitle()">
          {{ state.groupDescription }}
        </ui-page-header>

        <h2>{{ subtitle }}</h2>

        <ui-error />

        <ui-monitor-route-form
          mode="add"
          [groupName]="state.groupName"
          [initialProperties]="{
            groupName: state.groupName,
          }"
        />
      </ui-page>
    }
  `,
  providers: [MonitorRouteAddPageService, NavService],
  imports: [
    ErrorComponent,
    MonitorRouteFormComponent,
    PageComponent,
    PageHeaderComponent,
    BreadcrumbComponent,
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
