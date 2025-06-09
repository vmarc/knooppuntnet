import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';

@Component({
  selector: 'ui-subset-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-menu>
      <ui-page-menu-option
        [link]="link('networks')"
        [active]="pageName() === 'networks'"
        [elementCount]="subsetInfo()?.networkCount"
        i18n="@@subset-page.menu.networks"
      >
        Networks
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="link('facts')"
        [active]="pageName() === 'facts'"
        [elementCount]="subsetInfo()?.factCount"
        i18n="@@subset-page.menu.facts"
      >
        Facts
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="link('orphan-nodes')"
        [active]="pageName() === 'orphan-nodes'"
        [elementCount]="subsetInfo()?.orphanNodeCount"
        i18n="@@subset-page.menu.orphan-nodes"
      >
        Orphan nodes
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="link('orphan-routes')"
        [active]="pageName() === 'orphan-routes'"
        [elementCount]="subsetInfo()?.orphanRouteCount"
        i18n="@@subset-page.menu.orphan-routes"
      >
        Free routes
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="link('map')"
        [active]="pageName() === 'map'"
        i18n="@@subset-page.menu.map"
      >
        Map
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="link('changes')"
        [active]="pageName() === 'changes'"
        i18n="@@subset-page.menu.changes"
      >
        Changes
      </ui-page-menu-option>
    </ui-page-menu>
  `,
  imports: [PageMenuComponent, PageMenuOptionComponent],
})
export class SubsetPageMenuComponent {
  readonly subset = input.required<Subset>();
  readonly subsetInfo = input.required<SubsetInfo>();
  readonly pageName = input.required<string>();

  link(targetPageName: string) {
    return `/analysis/${this.subset().routeType}/${this.subset().country}/${targetPageName}`;
  }
}
