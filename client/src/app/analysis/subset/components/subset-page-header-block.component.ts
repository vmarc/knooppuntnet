import {Component, Input} from "@angular/core";
import {Util} from "../../../components/shared/util";
import {I18nService} from "../../../i18n/i18n.service";
import {SubsetInfo} from "../../../kpn/api/common/subset/subset-info";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetCacheService} from "../../../services/subset-cache.service";

@Component({
  selector: "kpn-subset-page-header-block",
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li>
        <a routerLink="{{networkTypeLink()}}">
          <kpn-network-type-name [networkType]="subset.networkType"></kpn-network-type-name>
        </a>
      </li>
      <li>
        <a routerLink="{{countryLink()}}">
          <kpn-country-name [country]="subset.country"></kpn-country-name>
        </a>
      </li>
      <li>
        <span *ngIf="pageName == 'networks'" i18n="@@subset-page.menu.networks">Networks</span>
        <span *ngIf="pageName == 'facts'" i18n="@@subset-page.menu.facts">Facts</span>
        <span *ngIf="pageName == 'orphan-nodes'" i18n="@@subset-page.menu.orphan-nodes">Orphan nodes</span>
        <span *ngIf="pageName == 'orphan-routes'" i18n="@@subset-page.menu.orphan-routes">Orphan routes</span>
        <span *ngIf="pageName == 'map'" i18n="@@subset-page.menu.map">Map</span>
        <span *ngIf="pageName == 'changes'" i18n="@@subset-page.menu.changes">Changes</span>
      </li>
    </ul>

    <kpn-page-header [pageTitle]="subsetPageTitle()" [subject]="'subset-' + pageName + '-page'">
      {{subsetName()}}
    </kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        [link]="link('networks')"
        [elementCount]="networkCount()"
        i18n="@@subset-page.menu.networks">
        Networks
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('facts')"
        [elementCount]="factCount()"
        i18n="@@subset-page.menu.facts">
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-nodes')"
        [elementCount]="orphanNodeCount()"
        i18n="@@subset-page.menu.orphan-nodes">
        Orphan nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-routes')"
        [elementCount]="orphanRouteCount()"
        i18n="@@subset-page.menu.orphan-routes">
        Orphan routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('map')"
        i18n="@@subset-page.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('changes')"
        i18n="@@subset-page.menu.changes">
        Changes
      </kpn-page-menu-option>

    </kpn-page-menu>
  `
})
export class SubsetPageHeaderBlockComponent {

  @Input() subset: Subset;
  @Input() pageName: string;
  @Input() pageTitle: string;

  constructor(private subsetCacheService: SubsetCacheService,
              private i18nService: I18nService) {
  }

  networkTypeLink(): string {
    const networkType = Util.safeGet(() => this.subset.networkType.name);
    return `/analysis/${networkType}`;
  }

  countryLink(): string {
    const networkType = Util.safeGet(() => this.subset.networkType.name);
    const country = Util.safeGet(() => this.subset.country.domain);
    return `/analysis/${networkType}/${country}`;
  }

  link(targetPageName: string) {
    if (this.subset != null) {
      const networkType = Util.safeGet(() => this.subset.networkType.name);
      const country = Util.safeGet(() => this.subset.country.domain);
      return `/analysis/${networkType}/${country}/${targetPageName}`;
    }
    return "/";
  }

  networkCount() {
    return Util.safeGet(() => this.subsetInfo().networkCount);
  }

  factCount() {
    return Util.safeGet(() => this.subsetInfo().factCount);
  }

  orphanNodeCount() {
    return Util.safeGet(() => this.subsetInfo().orphanNodeCount);
  }

  orphanRouteCount() {
    return Util.safeGet(() => this.subsetInfo().orphanRouteCount);
  }

  subsetInfo(): SubsetInfo {
    return this.subsetCacheService.getSubsetInfo(this.subset.key());
  }

  subsetName(): string {
    const networkType = this.i18nService.translation("@@network-type." + this.subset.networkType.name);
    const country = this.i18nService.translation("@@country." + this.subset.country.domain);
    const inWord = this.i18nService.translation("@@subset.in");
    return `${networkType} ${inWord} ${country}`;
  }

  subsetPageTitle(): string {
    return this.subsetName() + " | " + this.pageTitle;
  }
}
