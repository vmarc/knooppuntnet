import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { LocationKey } from '@api/custom';
import { RouteTypeNameComponent } from '@app/components/shared';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { LocationPipe } from '../../../shared/components/shared/format/location.pipe';
import { LocationService } from '../location.service';
import { LocationPageBreadcrumbComponent } from './location-page-breadcrumb.component';

@Component({
  selector: 'kpn-location-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.key(); as key) {
      <kpn-location-page-breadcrumb [locationKey]="key" />
      <kpn-page-header [pageTitle]="fullPageTitle()" subject="location-page">
        <span class="header-route-type-icon">
          <nz-icon [nzType]="key.routeType" />
        </span>
        <kpn-route-type-name [routeType]="key.routeType" />&nbsp;
        <span i18n="@@location-page.header.in">in</span>
        {{ locationName(key) | location }}
      </kpn-page-header>
      @if (service.summary(); as summary) {
        <kpn-page-menu>
          <kpn-page-menu-option
            [link]="link(key, 'details')"
            [active]="pageName() === 'details'"
            i18n="@@location-page.menu.details"
          >
            Details
          </kpn-page-menu-option>
          <kpn-page-menu-option
            [link]="link(key, 'nodes')"
            [active]="pageName() === 'nodes'"
            i18n="@@location-page.menu.nodes"
            [elementCount]="summary.nodeCount"
          >
            Nodes
          </kpn-page-menu-option>
          <kpn-page-menu-option
            [link]="link(key, 'routes')"
            [active]="pageName() === 'routes'"
            i18n="@@location-page.menu.routes"
            [elementCount]="summary.routeCount"
          >
            Routes
          </kpn-page-menu-option>
          <kpn-page-menu-option
            [link]="link(key, 'facts')"
            [active]="pageName() === 'facts'"
            i18n="@@location-page.menu.facts"
            [elementCount]="summary.factCount"
          >
            Facts
          </kpn-page-menu-option>
          <kpn-page-menu-option
            [link]="link(key, 'map')"
            [active]="pageName() === 'map'"
            i18n="@@location-page.menu.map"
          >
            Map
          </kpn-page-menu-option>
          <kpn-page-menu-option
            [link]="link(key, 'changes')"
            [active]="pageName() === 'changes'"
            i18n="@@location-page.menu.changes"
            [elementCount]="summary.changesCount"
          >
            Changes
          </kpn-page-menu-option>
          <kpn-page-menu-option
            [link]="link(key, 'edit')"
            [active]="pageName() === 'edit'"
            i18n="@@location-page.menu.edit"
          >
            Load in editor
          </kpn-page-menu-option>
        </kpn-page-menu>
      }
    }
  `,
  imports: [
    LocationPageBreadcrumbComponent,
    LocationPipe,
    MatIconModule,
    NzIconDirective,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
    RouteTypeNameComponent,
  ],
})
export class LocationPageHeaderComponent {
  pageName = input.required<string>();
  pageTitle = input.required<string>();

  protected readonly service = inject(LocationService);

  readonly fullPageTitle = computed(() => `${this.service.key().name} | ${this.pageTitle()}`);

  locationName(locationKey: LocationKey): string {
    const nameParts = locationKey.name.split(':');
    return nameParts[nameParts.length - 1];
  }

  link(locationKey: LocationKey, target: string): string {
    const key = `${locationKey.routeType}/${locationKey.country}/${locationKey.name}`;
    return `/analysis/${key}/${target}`;
  }
}
