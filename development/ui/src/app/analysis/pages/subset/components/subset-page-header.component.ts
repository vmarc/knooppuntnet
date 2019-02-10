import {Component, Input} from '@angular/core';
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetCacheService} from "../../../../services/subset-cache.service";
import {SubsetInfo} from "../../../../kpn/shared/subset/subset-info";
import {Util} from "../../../../components/shared/util";

@Component({
  selector: 'kpn-subset-page-header',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      <a routerLink="{{countryLink()}}"><kpn-country-name [country]="subset.country"></kpn-country-name></a> >
      <kpn-network-type-name [networkType]="subset.networkType"></kpn-network-type-name>
    </div>

    <h1>
      <kpn-subset-name [subset]="subset"></kpn-subset-name>
    </h1>

    <kpn-page-menu>

      <kpn-page-menu-option
        pageName="networks"
        selectedPageName="{{pageName}}"
        link="{{link('networks')}}"
        pageTitle="Networks"
        [elementCount]="networkCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="facts"
        selectedPageName="{{pageName}}"
        link="{{link('facts')}}"
        pageTitle="Facts"
        [elementCount]="factCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="orphan-nodes"
        selectedPageName="{{pageName}}"
        link="{{link('orphan-nodes')}}"
        pageTitle="Orphan Nodes"
        [elementCount]="orphanNodeCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="orphan-routes"
        selectedPageName="{{pageName}}"
        link="{{link('orphan-routes')}}"
        pageTitle="Orphan routes"
        [elementCount]="orphanRouteCount()">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="changes"
        selectedPageName="{{pageName}}"
        link="{{link('changes')}}"
        pageTitle="Changes">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class SubsetPageHeaderComponent {

  @Input() subset: Subset;
  @Input() pageName: string;

  constructor(private subsetCacheService: SubsetCacheService) {
  }

  private countryLink(): string {
    return "/analysis/" + Util.safeGet(() => this.subset.country.domain);
  }

  private link(targetPageName: string) {
    if (this.subset != null) {
      return "/analysis/" + targetPageName + "/" + this.subset.country.domain + "/" + this.subset.networkType.name;
    }
    return "/";
  }

  private networkCount() {
    return Util.safeGet(() => this.subsetInfo().networkCount);
  }

  private factCount() {
    return Util.safeGet(() => this.subsetInfo().factCount);
  }

  private orphanNodeCount() {
    return Util.safeGet(() => this.subsetInfo().orphanNodeCount);
  }

  private orphanRouteCount() {
    return Util.safeGet(() => this.subsetInfo().orphanRouteCount);
  }

  private subsetInfo(): SubsetInfo {
    return this.subsetCacheService.getSubsetInfo(this.subset.key());
  }

}
