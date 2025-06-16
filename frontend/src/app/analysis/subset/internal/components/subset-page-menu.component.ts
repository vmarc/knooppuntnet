import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';

@Component({
  selector: 'ui-subset-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-page-menu [pageName]="pageName()" [options]="menuOptions()" /> `,
  imports: [PageMenuComponent],
})
export class SubsetPageMenuComponent {
  readonly subset = input.required<Subset>();
  readonly subsetInfo = input.required<SubsetInfo>();
  readonly pageName = input.required<string>();

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    const link = `/analysis/${this.subset().routeType}/${this.subset().country}/`;
    const subsetInfo = this.subsetInfo();
    return [
      {
        pageName: 'networks',
        pageLink: link + 'networks',
        label: $localize`:@@subset-page.menu.networks:Networks`,
        elementCount: subsetInfo?.networkCount,
      },
      {
        pageName: 'facts',
        pageLink: link + 'facts',
        label: $localize`:@@subset-page.menu.facts:Facts`,
        elementCount: subsetInfo?.factCount,
      },
      {
        pageName: 'orphan-nodes',
        pageLink: link + 'orphan-nodes',
        label: $localize`:@@subset-page.menu.orphan-nodes:Orphan nodes`,
        elementCount: subsetInfo?.orphanNodeCount,
      },
      {
        pageName: 'orphan-routes',
        pageLink: link + 'orphan-routes',
        label: $localize`:@@subset-page.menu.orphan-routes:Free routes`,
        elementCount: subsetInfo?.orphanRouteCount,
      },
      { pageName: 'map', pageLink: link + 'map', label: $localize`:@@subset-page.menu.map:Map` },
      {
        pageName: 'changes',
        pageLink: link + 'changes',
        label: $localize`:@@subset-page.menu.changes:Changes`,
      },
    ];
  });
}
