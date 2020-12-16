import {ChangeDetectionStrategy} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {LocationKey} from '@api/custom/location-key';
import {LocationService} from '../location.service';

@Component({
  selector: 'kpn-location-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="locationKey$ | async as locationKey">

      <kpn-location-page-breadcrumb [locationKey]="locationKey"></kpn-location-page-breadcrumb>

      <kpn-page-header [pageTitle]="locationPageTitle()" subject="location-page" i18n="@@location-page.header">
        <kpn-network-type-name [networkType]="locationKey.networkType"></kpn-network-type-name>
        in
        {{locationKey.name}}
      </kpn-page-header>

      <kpn-page-menu>

        <kpn-page-menu-option
          [link]="link('nodes')"
          [active]="pageName === 'nodes'"
          i18n="@@location-page.menu.nodes"
          [elementCount]="nodeCount$ | async">
          Nodes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('routes')"
          [active]="pageName === 'routes'"
          i18n="@@location-page.menu.routes"
          [elementCount]="routeCount$ | async">
          Routes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('facts')"
          [active]="pageName === 'facts'"
          i18n="@@location-page.menu.facts"
          [elementCount]="factCount$ | async">
          Facts
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('map')"
          [active]="pageName === 'map'"
          i18n="@@location-page.menu.map">
          Map
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('changes')"
          [active]="pageName === 'changes'"
          i18n="@@location-page.menu.changes"
          [elementCount]="changeCount$ | async">
          Changes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('edit')"
          [active]="pageName === 'edit'"
          i18n="@@location-page.menu.edit">
          Load in editor
        </kpn-page-menu-option>

      </kpn-page-menu>
    </ng-container>
  `
})
export class LocationPageHeaderComponent {

  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly locationKey$: Observable<LocationKey>;
  readonly nodeCount$: Observable<number>;
  readonly routeCount$: Observable<number>;
  readonly factCount$: Observable<number>;
  readonly changeCount$: Observable<number>;

  constructor(private service: LocationService) {
    this.locationKey$ = service.locationKey$;
    this.nodeCount$ = service.summary$.pipe(map(summary => summary.nodeCount));
    this.routeCount$ = service.summary$.pipe(map(summary => summary.routeCount));
    this.factCount$ = service.summary$.pipe(map(summary => summary.factCount));
    this.changeCount$ = service.summary$.pipe(map(summary => summary.changeCount));
  }

  link(target: string): string {
    return `/analysis/${this.service.key}/${target}`;
  }

  locationPageTitle(): string {
    return `${this.service.name} | ${this.pageTitle}`;
  }

}
