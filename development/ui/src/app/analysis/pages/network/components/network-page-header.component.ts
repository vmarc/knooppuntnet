import {Component, Input} from '@angular/core';
import {NetworkCacheService} from "../../../../services/network-cache.service";

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
        pageName="network-nodes"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-nodes/' + networkId}}"
        pageTitle="Nodes"
        [elementCount]="nodeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="network-routes"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-routes/' + networkId}}"
        pageTitle="Routes"
        [elementCount]="routeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="network-map"
        selectedPageName="{{selectedPage}}"
        link="{{'/analysis/network-map/' + networkId}}"
        pageTitle="Map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="network-changes"
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

  isNetworkNameKnown(): boolean {
    return this.networkId && this.networkCacheService.getNetworkName(this.networkId) !== undefined;
  }

  networkName(): string {
    return this.networkCacheService.getNetworkName(this.networkId);
  }

  factCount() {
    const summary = this.networkCacheService.getNetworkSummary(this.networkId);
    if (summary != null) {
      return summary.factCount;
    }
    return null;
  }

  nodeCount() {
    const summary = this.networkCacheService.getNetworkSummary(this.networkId);
    if (summary != null) {
      return summary.nodeCount;
    }
    return null;
  }

  routeCount() {
    const summary = this.networkCacheService.getNetworkSummary(this.networkId);
    if (summary != null) {
      return summary.routeCount;
    }
    return null;
  }

}
