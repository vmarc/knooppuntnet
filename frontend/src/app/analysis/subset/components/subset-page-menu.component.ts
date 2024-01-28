import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SubsetInfo } from '@api/common/subset';
import { Subset } from '@api/custom';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';

@Component({
  selector: 'kpn-subset-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="link('networks')"
        [active]="pageName() === 'networks'"
        [elementCount]="subsetInfo()?.networkCount"
        i18n="@@subset-page.menu.networks"
      >
        Networks
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('facts')"
        [active]="pageName() === 'facts'"
        [elementCount]="subsetInfo()?.factCount"
        i18n="@@subset-page.menu.facts"
      >
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-nodes')"
        [active]="pageName() === 'orphan-nodes'"
        [elementCount]="subsetInfo()?.orphanNodeCount"
        i18n="@@subset-page.menu.orphan-nodes"
      >
        Orphan nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-routes')"
        [active]="pageName() === 'orphan-routes'"
        [elementCount]="subsetInfo()?.orphanRouteCount"
        i18n="@@subset-page.menu.orphan-routes"
      >
        Free routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('map')"
        [active]="pageName() === 'map'"
        i18n="@@subset-page.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('changes')"
        [active]="pageName() === 'changes'"
        i18n="@@subset-page.menu.changes"
      >
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
  standalone: true,
  imports: [PageMenuComponent, PageMenuOptionComponent],
})
export class SubsetPageMenuComponent {
  subset = input<Subset | undefined>();
  subsetInfo = input<SubsetInfo | undefined>();
  pageName = input<string | undefined>();

  link(targetPageName: string) {
    return `/analysis/${this.subset().networkType}/${this.subset().country}/${targetPageName}`;
  }
}
