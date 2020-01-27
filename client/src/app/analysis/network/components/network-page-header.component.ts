import {Component, Input} from "@angular/core";
import {Util} from "../../../components/shared/util";
import {NetworkSummary} from "../../../kpn/api/common/network/network-summary";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: "kpn-network-page-header",
  template: `

    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@breadcrumb.network">Network</ng-container>
    </div>

    <kpn-page-header [pageTitle]="networkPageTitle()" subject="network-page">{{networkName()}}</kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
          link="{{'/analysis/network/' + networkId}}"
          i18n="@@network-page.menu.details">
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
          link="{{'/analysis/network/' + networkId + '/facts'}}"
          i18n="@@network-page.menu.facts"
          [elementCount]="factCount()">
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
          link="{{'/analysis/network/' + networkId + '/nodes'}}"
          i18n="@@network-page.menu.nodes"
          [elementCount]="nodeCount()">
        Nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
          link="{{'/analysis/network/' + networkId + '/routes'}}"
          i18n="@@network-page.menu.routes"
          [elementCount]="routeCount()">
        Routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
          link="{{'/analysis/network/' + networkId + '/map'}}"
          i18n="@@network-page.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
          link="{{'/analysis/network/' + networkId + '/changes'}}"
          i18n="@@network-page.menu.changes"
          [elementCount]="changeCount()">
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

  changeCount() {
    return Util.safeGet(() => this.networkSummary().changeCount);
  }

  private networkSummary(): NetworkSummary {
    return this.networkCacheService.getNetworkSummary(this.networkId);
  }

}
