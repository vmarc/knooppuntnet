import { inject } from '@angular/core';
import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
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
    @if (summary()) {
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
      <ui-page-header [pageTitle]="pageTitle()">
        {{ pageHeader() }}
      </ui-page-header>

      <ui-page-menu>
        <ui-page-menu-option
          [link]="routeLink()"
          [active]="pageName() === 'details'"
          [state]="routeLinkState()"
          i18n="@@monitor.route.menu.details"
        >
          Details
        </ui-page-menu-option>

        <ui-page-menu-option
          [link]="routeMembersLink()"
          [active]="pageName() === 'members'"
          [state]="routeLinkState()"
          i18n="@@monitor.route.menu.members"
          [elementCount]="memberCount()"
        >
          Members
        </ui-page-menu-option>

        <ui-page-menu-option
          [link]="routeSegmentsLink()"
          [active]="pageName() === 'segments'"
          [state]="routeLinkState()"
          i18n="@@monitor.route.menu.segments"
          [elementCount]="segmentCount()"
        >
          Segments
        </ui-page-menu-option>

        <ui-page-menu-option
          [link]="routeDeviationsLink()"
          [active]="pageName() === 'deviations'"
          [state]="routeLinkState()"
          i18n="@@monitor.route.menu.deviations"
          [elementCount]="deviationCount()"
        >
          Deviations
        </ui-page-menu-option>
      </ui-page-menu>
    }

    <ui-error />
  `,
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
})
export class MonitorRoutePageHeaderComponent {
  private readonly monitorRouteService = inject(MonitorRouteService);

  readonly pageName = input.required<string>();

  protected readonly summary = computed(() => this.monitorRouteService.summary());

  protected readonly pageTitle = computed(() => {
    const monitor = MonitorTranslations.get('monitor');
    return `${this.summary().routeName} | ${this.summary().groupName} | ${monitor}`;
  });

  protected readonly pageHeader = computed(() => {
    return `${this.summary().routeName}: ${this.summary().routeDescription}`;
  });

  protected readonly memberCount = computed(() => this.summary().memberCount);
  protected readonly segmentCount = computed(() => this.summary().segmentCount);
  protected readonly deviationCount = computed(() => this.summary().deviationCount);

  protected readonly groupLink = computed(() => `/monitor/groups/${this.summary().groupName}`);
  protected readonly routeLink = computed(
    () => `${this.groupLink()}/routes/${this.summary().routeName}`
  );
  protected readonly routeMembersLink = computed(() => `${this.routeLink()}/members`);
  protected readonly routeSegmentsLink = computed(() => `${this.routeLink()}/segments`);
  protected readonly routeDeviationsLink = computed(() => `${this.routeLink()}/deviations`);

  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    return [
      Breadcrumbs.home,
      Breadcrumbs.monitor,
      { routerLink: this.groupLink(), label: this.summary().groupName },
      { label: Breadcrumbs.monitorRouteLabel },
    ];
  });

  routeLinkState() {
    return { description: this.summary().routeDescription };
  }
}
