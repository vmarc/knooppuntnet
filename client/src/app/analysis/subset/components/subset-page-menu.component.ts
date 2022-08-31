import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';

@Component({
  selector: 'kpn-subset-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="link('networks')"
        [active]="pageName === 'networks'"
        [elementCount]="subsetInfo?.networkCount"
        i18n="@@subset-page.menu.networks"
      >
        Networks
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('facts')"
        [active]="pageName === 'facts'"
        [elementCount]="subsetInfo?.factCount"
        i18n="@@subset-page.menu.facts"
      >
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-nodes')"
        [active]="pageName === 'orphan-nodes'"
        [elementCount]="subsetInfo?.orphanNodeCount"
        i18n="@@subset-page.menu.orphan-nodes"
      >
        Orphan nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-routes')"
        [active]="pageName === 'orphan-routes'"
        [elementCount]="subsetInfo?.orphanRouteCount"
        i18n="@@subset-page.menu.orphan-routes"
      >
        Free routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('map')"
        [active]="pageName === 'map'"
        i18n="@@subset-page.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('changes')"
        [active]="pageName === 'changes'"
        i18n="@@subset-page.menu.changes"
      >
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
})
export class SubsetPageMenuComponent {
  @Input() subset: Subset;
  @Input() subsetInfo: SubsetInfo;
  @Input() pageName: string;

  link(targetPageName: string) {
    return `/analysis/${this.subset.networkType}/${this.subset.country}/${targetPageName}`;
  }
}
