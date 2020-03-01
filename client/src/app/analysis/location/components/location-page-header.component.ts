import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {LocationSummary} from "../../../kpn/api/common/location/location-summary";
import {LocationService} from "../location.service";

@Component({
  selector: "kpn-location-page-header",
  template: `
    <ng-container *ngIf="service.locationKey | async as locationKey">

      <kpn-location-page-breadcrumb [locationKey]="locationKey"></kpn-location-page-breadcrumb>

      <kpn-page-header [pageTitle]="locationPageTitle()" subject="location-page" i18n="@@location-page.header">
        <kpn-network-type-name [networkType]="locationKey.networkType"></kpn-network-type-name>
        in
        {{locationKey.name}}
      </kpn-page-header>

      <kpn-page-menu>
        <kpn-page-menu-option
          [link]="link('nodes')"
          i18n="@@location-page.menu.nodes"
          [elementCount]="summary?.nodeCount">
          Nodes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('routes')"
          i18n="@@location-page.menu.routes"
          [elementCount]="summary?.routeCount">
          Routes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('facts')"
          i18n="@@location-page.menu.facts"
          [elementCount]="summary?.factCount">
          Facts
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('map')"
          i18n="@@location-page.menu.map">
          Map
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('changes')"
          i18n="@@location-page.menu.changes"
          [elementCount]="summary?.changeCount">
          Changes
        </kpn-page-menu-option>

      </kpn-page-menu>
    </ng-container>
  `
})
export class LocationPageHeaderComponent {

  @Input() pageTitle;

  constructor(public service: LocationService) {
  }

  get summary(): LocationSummary {
    return this.service.summary;
  }

  link(target: string): string {
    return `/analysis/${this.service.key}/${target}`;
  }

  locationPageTitle(): string {
    return `${this.service.name} | ${this.pageTitle}`;
  }

}
