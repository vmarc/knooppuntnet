import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationKey } from '@api/custom/location-key';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { selectLocationFactCount } from '../store/location.selectors';
import { selectLocationRouteCount } from '../store/location.selectors';
import { selectLocationChangeCount } from '../store/location.selectors';
import { selectLocationNodeCount } from '../store/location.selectors';
import { selectLocationKey } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="locationKey$ | async as locationKey">
      <kpn-location-page-breadcrumb
        [locationKey]="locationKey"
      ></kpn-location-page-breadcrumb>

      <kpn-page-header [pageTitle]="pageTitle$ | async" subject="location-page">
        <span class="header-network-type-icon">
          <mat-icon [svgIcon]="locationKey.networkType"></mat-icon>
        </span>
        <kpn-network-type-name
          [networkType]="locationKey.networkType"
        ></kpn-network-type-name>
        <span i18n="@@location-page.header.in">in</span>
        {{ locationName(locationKey) }}
      </kpn-page-header>

      <kpn-page-menu>
        <kpn-page-menu-option
          [link]="nodesLink$ | async"
          [active]="pageName === 'nodes'"
          i18n="@@location-page.menu.nodes"
          [elementCount]="nodeCount$ | async"
        >
          Nodes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="routesLink$ | async"
          [active]="pageName === 'routes'"
          i18n="@@location-page.menu.routes"
          [elementCount]="routeCount$ | async"
        >
          Routes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="factsLink$ | async"
          [active]="pageName === 'facts'"
          i18n="@@location-page.menu.facts"
          [elementCount]="factCount$ | async"
        >
          Facts
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="mapLink$ | async"
          [active]="pageName === 'map'"
          i18n="@@location-page.menu.map"
        >
          Map
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="changesLink$ | async"
          [active]="pageName === 'changes'"
          i18n="@@location-page.menu.changes"
          [elementCount]="changeCount$ | async"
        >
          Changes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="editLink$ | async"
          [active]="pageName === 'edit'"
          i18n="@@location-page.menu.edit"
        >
          Load in editor
        </kpn-page-menu-option>
      </kpn-page-menu>
    </ng-container>
  `,
})
export class LocationPageHeaderComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly locationKey$ = this.store.select(selectLocationKey);
  readonly nodeCount$ = this.store.select(selectLocationNodeCount);
  readonly routeCount$ = this.store.select(selectLocationRouteCount);
  readonly factCount$ = this.store.select(selectLocationFactCount);
  readonly changeCount$ = this.store.select(selectLocationChangeCount);

  readonly nodesLink$ = this.link('nodes');
  readonly routesLink$ = this.link('routes');
  readonly factsLink$ = this.link('facts');
  readonly mapLink$ = this.link('map');
  readonly changesLink$ = this.link('changes');
  readonly editLink$ = this.link('edit');

  readonly pageTitle$ = this.locationKey$.pipe(
    map((locationKey) => {
      const key = `${locationKey.networkType}/${locationKey.country}/${locationKey.name}`;
      return `${locationKey.name} | ${this.pageTitle}`;
    })
  );

  constructor(private store: Store<AppState>) {}

  locationName(locationKey: LocationKey): string {
    const nameParts = locationKey.name.split(':');
    return nameParts[nameParts.length - 1];
  }

  private link(target: string): Observable<string> {
    return this.locationKey$.pipe(
      map((locationKey) => {
        const key = `${locationKey.networkType}/${locationKey.country}/${locationKey.name}`;
        return `/analysis/${key}/${target}`;
      })
    );
  }
}
