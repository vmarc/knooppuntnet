import {Component, Input} from "@angular/core";
import {NetworkSummary} from "../../../kpn/api/common/network/network-summary";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: "kpn-network-page-header",
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.network">Network</li>
    </ul>

    <kpn-page-header [pageTitle]="networkPageTitle()" subject="network-page">{{networkName()}}</kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId"
        i18n="@@network-page.menu.details">
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/facts'"
        i18n="@@network-page.menu.facts"
        [elementCount]="summary?.factCount">
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/nodes'"
        i18n="@@network-page.menu.nodes"
        [elementCount]="summary?.nodeCount">
        Nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/routes'"
        i18n="@@network-page.menu.routes"
        [elementCount]="summary?.routeCount">
        Routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/map'"
        i18n="@@network-page.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/changes'"
        i18n="@@network-page.menu.changes"
        [elementCount]="summary?.changeCount">
        Changes
      </kpn-page-menu-option>

    </kpn-page-menu>
  `
})
export class NetworkPageHeaderComponent {

  @Input() networkId;
  @Input() pageTitle;

  constructor(private networkCacheService: NetworkCacheService) {
  }

  get summary(): NetworkSummary {
    return this.networkCacheService.getNetworkSummary(this.networkId);
  }

  networkName(): string {
    return this.networkCacheService.getNetworkName(this.networkId);
  }

  networkPageTitle(): string {
    const networkName = this.networkCacheService.getNetworkName(this.networkId);
    if (networkName) {
      return `${networkName} | ${this.pageTitle}`;
    }
    return null;
  }

}
