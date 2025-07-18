import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { MonitorTranslations } from '../../components/monitor-translations';

@Component({
  selector: 'ui-monitor-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
    <ui-page-header [pageTitle]="pageTitle()">
      {{ pageHeader() }}
    </ui-page-header>

    <ui-page-menu>
      <ui-page-menu-option
        [link]="routeDetailLink()"
        [active]="pageName() === 'details'"
        [state]="routeLinkState()"
        i18n="@@monitor.route.menu.details"
      >
        Details
      </ui-page-menu-option>
    </ui-page-menu>

    <ui-error />
  `,
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    MatMenuModule,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
})
export class MonitorRoutePageHeaderComponent {
  readonly pageName = input.required<string>();
  readonly groupName = input.required<string>();
  readonly routeName = input.required<string>();
  readonly routeDescription = input.required<string>();

  protected readonly pageTitle = computed(() => {
    const monitor = MonitorTranslations.get('monitor');
    return `${this.routeName()} | ${this.groupName()} | ${monitor}`;
  });

  protected readonly pageHeader = computed(() => {
    return `${this.routeName()}: ${this.routeDescription()}`;
  });

  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    return [
      Breadcrumbs.home,
      Breadcrumbs.monitor,
      { routerLink: this.groupLink(), label: this.groupName() },
      { label: Breadcrumbs.monitorRouteLabel },
    ];
  });

  groupLink(): string {
    return `/monitor/groups/${this.groupName()}`;
  }

  routeDetailLink(): string {
    return `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`;
  }

  routeLinkState() {
    return { description: this.routeDescription() };
  }
}
