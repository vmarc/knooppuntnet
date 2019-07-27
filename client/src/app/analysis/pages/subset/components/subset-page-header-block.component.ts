import {Component, Input} from "@angular/core";
import {Util} from "../../../../components/shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetInfo} from "../../../../kpn/shared/subset/subset-info";
import {SubsetCacheService} from "../../../../services/subset-cache.service";
import {I18nService} from "../../../../i18n/i18n.service";

@Component({
  selector: "kpn-subset-page-header-block",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <a routerLink="{{countryLink()}}">
        <kpn-country-name [country]="subset.country"></kpn-country-name>
      </a> >
      <kpn-network-type-name [networkType]="subset.networkType"></kpn-network-type-name>
    </div>

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
        Orphan Nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-routes')"
        [elementCount]="orphanRouteCount()"
        i18n="@@subset-page.menu.orphan-routes">
        Orphan routes
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

  countryLink(): string {
    return "/analysis/" + Util.safeGet(() => this.subset.country.domain);
  }

  link(targetPageName: string) {
    if (this.subset != null) {
      return `/analysis/${this.subset.country.domain}/${this.subset.networkType.name}/${targetPageName}`;
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
