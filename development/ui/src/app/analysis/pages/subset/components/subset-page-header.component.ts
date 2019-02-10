import {Component, Input} from '@angular/core';
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetCacheService} from "../../../../services/subset-cache.service";
import {SubsetInfo} from "../../../../kpn/shared/subset/subset-info";

@Component({
  selector: 'kpn-subset-page-header',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >

      <a routerLink="/analysis/nl" *ngIf="subset.country.domain === 'nl'">The Netherlands</a>
      <a routerLink="/analysis/be" *ngIf="subset.country.domain === 'be'">Belgium</a>
      <a routerLink="/analysis/de" *ngIf="subset.country.domain === 'de'">Germany</a> >

      <span *ngIf="subset.networkType.name === 'rcn'">Cycling</span>
      <span *ngIf="subset.networkType.name === 'rwn'">Hiking</span>
      <span *ngIf="subset.networkType.name === 'rhn'">Horse</span>
      <span *ngIf="subset.networkType.name === 'rmn'">Motorboat</span>
      <span *ngIf="subset.networkType.name === 'rpn'">Canoe</span>
      <span *ngIf="subset.networkType.name === 'rin'">Inline skating</span>
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

  private link(targetPageName: string) {
    if (this.subset != null) {
      return "/analysis/" + targetPageName + "/" + this.subset.country.domain + "/" + this.subset.networkType.name;
    }
    return "/";
  }

  networkCount() {
    const subsetInfo = this.subsetInfo();
    if (subsetInfo != null) {
      return subsetInfo.networkCount;
    }
    return null;
  }

  factCount() {
    const subsetInfo = this.subsetInfo();
    if (subsetInfo != null) {
      return subsetInfo.factCount;
    }
    return null;

  }

  orphanNodeCount() {
    const subsetInfo = this.subsetInfo();
    if (subsetInfo != null) {
      return subsetInfo.orphanNodeCount;
    }
    return null;

  }

  orphanRouteCount() {
    const subsetInfo = this.subsetInfo();
    if (subsetInfo != null) {
      return subsetInfo.orphanRouteCount;
    }
    return null;
  }

  private subsetInfo(): SubsetInfo {
    return this.subsetCacheService.getSubsetInfo(this.subset.key());
  }

}
