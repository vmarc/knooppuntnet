import {Component, Input} from '@angular/core';
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {NetworkSummary} from "../../../../kpn/shared/network/network-summary";
import {Util} from "../../../../components/shared/util";

@Component({
  selector: 'kpn-network-page-header',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      Network
    </div>

    <h1>
      {{networkName()}}
    </h1>

    <kpn-page-menu>
      <kpn-page-menu-option
        pageName="details"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-details/' + networkId}}"
        pageTitle="Details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="facts"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-facts/' + networkId}}"
        pageTitle="Facts"
        [elementCount]="factCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="nodes"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-nodes/' + networkId}}"
        pageTitle="Nodes"
        [elementCount]="nodeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="routes"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-routes/' + networkId}}"
        pageTitle="Routes"
        [elementCount]="routeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="map"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-map/' + networkId}}"
        pageTitle="Map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="changes"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-changes/' + networkId}}"
        pageTitle="Changes">
      </kpn-page-menu-option>

    </kpn-page-menu>
  `
})
export class NetworkPageHeaderComponent {

  @Input() networkId;
  @Input() selectedPage;

  constructor(private networkCacheService: NetworkCacheService) {
  }

  networkName(): string {
    return this.networkCacheService.getNetworkName(this.networkId);
  }

  factCount() {
    return Util.safeGet(() => this.networkSummary().factCount);
  }

  nodeCount() {
    return Util.safeGet(() => this.networkSummary().nodeCount);
  }

  routeCount() {
    return Util.safeGet(() => this.networkSummary().routeCount);
  }

  private networkSummary(): NetworkSummary {
    return this.networkCacheService.getNetworkSummary(this.networkId);
  }

}
