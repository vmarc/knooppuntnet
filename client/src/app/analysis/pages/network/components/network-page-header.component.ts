import {Component, Input} from "@angular/core";
import {Util} from "../../../../components/shared/util";
import {NetworkSummary} from "../../../../kpn/shared/network/network-summary";
import {NetworkCacheService} from "../../../../services/network-cache.service";

@Component({
  selector: "kpn-network-page-header",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@breadcrumb.network">Network</ng-container>
    </div>

    <kpn-page-header [pageTitle]="networkPageTitle()" subject="network-page">{{networkName()}}</kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
        link="{{'/analysis/network-details/' + networkId}}"
        pageTitle="Details"
        i18n-pageTitle="@@network-page.menu.details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        link="{{'/analysis/network-facts/' + networkId}}"
        pageTitle="Facts"
        i18n-pageTitle="@@network-page.menu.facts"
        [elementCount]="factCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        link="{{'/analysis/network-nodes/' + networkId}}"
        pageTitle="Nodes"
        i18n-pageTitle="@@network-page.menu.nodes"
        [elementCount]="nodeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        link="{{'/analysis/network-routes/' + networkId}}"
        pageTitle="Routes"
        i18n-pageTitle="@@network-page.menu.routes"
        [elementCount]="routeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        link="{{'/analysis/network-map/' + networkId}}"
        pageTitle="Map"
        i18n-pageTitle="@@network-page.menu.map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        link="{{'/analysis/network-changes/' + networkId}}"
        pageTitle="Changes"
        i18n-pageTitle="@@network-page.menu.changes">
      </kpn-page-menu-option>

    </kpn-page-menu>
  `
})
export class NetworkPageHeaderComponent {

  @Input() networkId;
  @Input() pageTitle;

  constructor(private networkCacheService: NetworkCacheService) {
  }

  networkName(): string {
    return this.networkCacheService.getNetworkName(this.networkId);
  }

  networkPageTitle(): string {
    const networkName = this.networkCacheService.getNetworkName(this.networkId);
    if (networkName) {
      return `${networkName} | ${this.pageTitle}`
    }
    return null;
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
