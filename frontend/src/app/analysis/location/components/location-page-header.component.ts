import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { LocationKey } from '@api/custom';
import { NetworkTypeNameComponent } from '@app/components/shared';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { selectLocationFactCount } from '../store/location.selectors';
import { selectLocationRouteCount } from '../store/location.selectors';
import { selectLocationChangeCount } from '../store/location.selectors';
import { selectLocationNodeCount } from '../store/location.selectors';
import { selectLocationKey } from '../store/location.selectors';
import { LocationPageBreadcrumbComponent } from './location-page-breadcrumb.component';

@Component({
  selector: 'kpn-location-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (locationKey(); as key) {
      <kpn-location-page-breadcrumb [locationKey]="key" />
      <kpn-page-header [pageTitle]="fullPageTitle()" subject="location-page">
        <span class="header-network-type-icon">
          <mat-icon [svgIcon]="key.networkType" />
        </span>
        <kpn-network-type-name [networkType]="key.networkType" />&nbsp;
        <span i18n="@@location-page.header.in">in</span>
        {{ locationName(key) }}
      </kpn-page-header>
      <kpn-page-menu>
        <kpn-page-menu-option
          [link]="nodesLink()"
          [active]="pageName() === 'nodes'"
          i18n="@@location-page.menu.nodes"
          [elementCount]="nodeCount()"
        >
          Nodes
        </kpn-page-menu-option>
        <kpn-page-menu-option
          [link]="routesLink()"
          [active]="pageName() === 'routes'"
          i18n="@@location-page.menu.routes"
          [elementCount]="routeCount()"
        >
          Routes
        </kpn-page-menu-option>
        <kpn-page-menu-option
          [link]="factsLink()"
          [active]="pageName() === 'facts'"
          i18n="@@location-page.menu.facts"
          [elementCount]="factCount()"
        >
          Facts
        </kpn-page-menu-option>
        <kpn-page-menu-option
          [link]="mapLink()"
          [active]="pageName() === 'map'"
          i18n="@@location-page.menu.map"
        >
          Map
        </kpn-page-menu-option>
        <kpn-page-menu-option
          [link]="changesLink()"
          [active]="pageName() === 'changes'"
          i18n="@@location-page.menu.changes"
          [elementCount]="changeCount()"
        >
          Changes
        </kpn-page-menu-option>
        <kpn-page-menu-option
          [link]="editLink()"
          [active]="pageName() === 'edit'"
          i18n="@@location-page.menu.edit"
        >
          Load in editor
        </kpn-page-menu-option>
      </kpn-page-menu>
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    LocationPageBreadcrumbComponent,
    MatIconModule,
    NetworkTypeNameComponent,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
})
export class LocationPageHeaderComponent {
  pageName = input.required<string>();
  pageTitle = input.required<string>();

  private readonly store = inject(Store);
  readonly locationKey = this.store.selectSignal(selectLocationKey);
  readonly nodeCount = this.store.selectSignal(selectLocationNodeCount);
  readonly routeCount = this.store.selectSignal(selectLocationRouteCount);
  readonly factCount = this.store.selectSignal(selectLocationFactCount);
  readonly changeCount = this.store.selectSignal(selectLocationChangeCount);

  readonly nodesLink = this.link('nodes');
  readonly routesLink = this.link('routes');
  readonly factsLink = this.link('facts');
  readonly mapLink = this.link('map');
  readonly changesLink = this.link('changes');
  readonly editLink = this.link('edit');

  readonly fullPageTitle = computed(() => `${this.locationKey().name} | ${this.pageTitle()}`);

  locationName(locationKey: LocationKey): string {
    const nameParts = locationKey.name.split(':');
    return nameParts[nameParts.length - 1];
  }

  private link(target: string): Signal<string> {
    return computed(() => {
      const lk = this.locationKey();
      const key = `${lk.networkType}/${lk.country}/${lk.name}`;
      return `/analysis/${key}/${target}`;
    });
  }
}
