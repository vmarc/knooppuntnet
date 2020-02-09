import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {LocationKey} from "../../../kpn/api/custom/location-key";

@Component({
  selector: "kpn-location-page-header",
  template: `
    <ng-container *ngIf="locationKey">

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
          [elementCount]="nodeCount()">
          Nodes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('routes')"
          i18n="@@location-page.menu.routes"
          [elementCount]="routeCount()">
          Routes
        </kpn-page-menu-option>

        <kpn-page-menu-option
          [link]="link('facts')"
          i18n="@@location-page.menu.facts"
          [elementCount]="factCount()">
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
          [elementCount]="changeCount()">
          Changes
        </kpn-page-menu-option>

      </kpn-page-menu>
    </ng-container>
  `
})
export class LocationPageHeaderComponent {

  @Input() locationKey: LocationKey;
  @Input() pageTitle;

  constructor(/*private networkCacheService: NetworkCacheService*/) {
  }

  link(target: string): string {
    return `/analysis/${this.locationKey.key()}/${target}`;
  }

  locationPageTitle(): string {
    return `${this.locationKey.name} | ${this.pageTitle}`;
  }

  factCount(): number {
    // return Util.safeGet(() => this.networkSummary().factCount);
    return 0;
  }

  nodeCount(): number {
    // return Util.safeGet(() => this.networkSummary().nodeCount);
    return 0;
  }

  routeCount(): number {
    // return Util.safeGet(() => this.networkSummary().routeCount);
    return 0;
  }

  changeCount(): number {
    // return Util.safeGet(() => this.networkSummary().changeCount);
    return 0;
  }

  // private networkSummary(): NetworkSummary {
  //   return this.networkCacheService.getNetworkSummary(this.networkId);
  // }

}
