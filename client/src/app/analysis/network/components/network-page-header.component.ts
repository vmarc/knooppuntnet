import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {NetworkSummary} from "../../../kpn/api/common/network/network-summary";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: "kpn-network-page-header",
  changeDetection: ChangeDetectionStrategy.OnPush,
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
        [active]="pageName === 'details'"
        i18n="@@network-page.menu.details">
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/facts'"
        [active]="pageName === 'facts'"
        [elementCount]="summary?.factCount"
        i18n="@@network-page.menu.facts">
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/nodes'"
        [active]="pageName === 'nodes'"
        [elementCount]="summary?.nodeCount"
        i18n="@@network-page.menu.nodes">
        Nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/routes'"
        [active]="pageName === 'routes'"
        [elementCount]="summary?.routeCount"
        i18n="@@network-page.menu.routes">
        Routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/map'"
        [active]="pageName === 'map'"
        i18n="@@network-page.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="'/analysis/network/' + networkId + '/changes'"
        [active]="pageName === 'changes'"
        [elementCount]="summary?.changeCount"
        i18n="@@network-page.menu.changes">
        Changes
      </kpn-page-menu-option>

    </kpn-page-menu>
  `
})
export class NetworkPageHeaderComponent {

  @Input() pageName: string;
  @Input() networkId: number;
  @Input() pageTitle: string;

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
