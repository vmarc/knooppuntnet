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
        {{countryName()}}
      </a> >
      <kpn-network-type-name [networkType]="subset.networkType"></kpn-network-type-name>
    </div>

    <kpn-page-header [pageTitle]="subsetPageTitle()" [subject]="'subset-' + pageName + '-page'">
      {{subsetName()}}
    </kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        pageName="networks"
        [selectedPageName]="pageName"
        [link]="link('networks')"
        pageTitle="Networks"
        i18n-pageTitle="@@subset-page.menu.networks"
        [elementCount]="networkCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="facts"
        [selectedPageName]="pageName"
        [link]="link('facts')"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-page.menu.facts"
        [elementCount]="factCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="orphan-nodes"
        [selectedPageName]="pageName"
        [link]="link('orphan-nodes')"
        pageTitle="Orphan Nodes"
        i18n-pageTitle="@@subset-page.menu.orphan-nodes"
        [elementCount]="orphanNodeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="orphan-routes"
        [selectedPageName]="pageName"
        [link]="link('orphan-routes')"
        pageTitle="Orphan routes"
        i18n-pageTitle="@@subset-page.menu.orphan-routes"
        [elementCount]="orphanRouteCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="changes"
        [selectedPageName]="pageName"
        [link]="link('changes')"
        pageTitle="Changes"
        i18n-pageTitle="@@subset-page.menu.changes">
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

  countryName(): string {
    return this.i18nService.name("country." + Util.safeGet(() => this.subset.country.domain));
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
    const networkType = this.i18nService.name("network-type." + this.subset.networkType.name);
    const country = this.i18nService.name("country." + this.subset.country.domain);
    const inWord = this.i18nService.name("subset.in");
    return `${networkType} ${inWord} ${country}`;
  }

  subsetPageTitle(): string {
    return this.subsetName() + " | " + this.pageTitle;
  }
}
