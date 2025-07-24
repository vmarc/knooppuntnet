import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationKey } from '@api/custom/location-key';
import { LocationPageName } from '@app/analysis/location/internal/components/location-page';
import { LocationPipe } from '@app/shared/components/format/location.pipe';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { RouteTypeNameComponent } from '@app/shared/components/route-type-name.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { LocationService } from '../location.service';
import { LocationPageBreadcrumbComponent } from './location-page-breadcrumb.component';

const PAGE_TITLE_MAP: Record<LocationPageName, string> = {
  details: $localize`:@@location-details.title:Details`,
  nodes: $localize`:@@location-nodes.title:Nodes`,
  routes: $localize`:@@location-routes.title:Routes`,
  facts: $localize`:@@location-facts.title:Facts`,
  map: $localize`:@@location-map.title:Map`,
  changes: $localize`:@@location-changes.title:Changes`,
  edit: $localize`:@@location-edit.title:Load in editor`,
};

@Component({
  selector: 'ui-location-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (locationKey(); as key) {
      <ui-location-page-breadcrumb [locationKey]="key" />
      <ui-page-header [pageTitle]="fullPageTitle()" subject="location-page">
        <span class="header-route-type-icon">
          <nz-icon [nzType]="key.routeType" />
        </span>
        <ui-route-type-name [routeType]="key.routeType" />&nbsp;
        <span i18n="@@location-page.header.in">in</span>
        {{ locationName(key) | location }}
      </ui-page-header>
      <ui-page-menu [pageName]="pageName()" [options]="menuOptions()" />
    }
  `,
  imports: [
    LocationPageBreadcrumbComponent,
    LocationPipe,
    NzIconDirective,
    PageHeaderComponent,
    PageMenuComponent,
    RouteTypeNameComponent,
  ],
})
export class LocationPageHeaderComponent {
  readonly pageName = input.required<LocationPageName>();

  private readonly service = inject(LocationService);

  protected readonly locationKey = this.service.key;

  protected readonly fullPageTitle = computed(
    () => `${this.service.key().name} | ${this.pageTitle()}`
  );

  protected readonly pageTitle = computed(() => {
    const pageName = this.service.pageName();
    return pageName ? PAGE_TITLE_MAP[pageName] : undefined;
  });

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    const summary = this.service.summary();
    const locationKey = this.service.key();
    const key = `${locationKey.routeType}/${locationKey.country}/${locationKey.name}`;
    const link = `/analysis/${key}/`;

    return [
      {
        pageName: 'details',
        pageLink: link + '/details',
        label: $localize`:@@location-page.menu.details:Details`,
      },
      {
        pageName: 'nodes',
        pageLink: link + '/nodes',
        label: $localize`:@@location-page.menu.nodes:Nodes`,
        elementCount: summary?.nodeCount,
      },
      {
        pageName: 'routes',
        pageLink: link + '/routes',
        label: $localize`:@@location-page.menu.routes:Routes`,
        elementCount: summary?.routeCount,
      },
      {
        pageName: 'facts',
        pageLink: link + '/facts',
        label: $localize`:@@location-page.menu.facts:Facts`,
        elementCount: summary?.factCount,
      },
      {
        pageName: 'map',
        pageLink: link + '/map',
        label: $localize`:@@location-page.menu.map:Map`,
      },
      {
        pageName: 'changes',
        pageLink: link + '/changes',
        label: $localize`:@@location-page.menu.changes:Changes`,
        elementCount: summary?.changesCount,
      },
      {
        pageName: 'edit',
        pageLink: link + '/edit',
        label: $localize`:@@location-page.menu.edit:Load in editor`,
      },
    ];
  });

  locationName(locationKey: LocationKey): string {
    const nameParts = locationKey.name.split(':');
    return nameParts[nameParts.length - 1];
  }
}
