import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Subset } from '@api/custom/subset';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { Util } from '@app/shared/components/util';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'ui-subset-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />',
  imports: [BreadcrumbComponent],
})
export class SubsetPageBreadcrumbComponent {
  readonly subset = input.required<Subset>();
  readonly pageName = input.required<string>();

  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    const subset = this.subset();
    const routeTypeLink = `/analysis/${subset.routeType}`;
    const routeTypeLabel = Translations.get('route-type.' + subset.routeType);
    const countryLink = `/analysis/${subset.routeType}/${subset.country}/networks`;
    const countryLabel = Translations.get('country.' + Util.safeGet(() => subset.country));

    let pageLabel = '';
    switch (this.pageName()) {
      case 'networks': {
        pageLabel = $localize`:@@subset-page.menu.networks:Networks`;
        break;
      }
      case 'facts': {
        pageLabel = $localize`:@@subset-page.menu.facts:Facts`;
        break;
      }
      case 'orphan-nodes': {
        pageLabel = $localize`:@@subset-page.menu.orphan-nodes:Orphan nodes`;
        break;
      }
      case 'orphan-routes': {
        pageLabel = $localize`:@@subset-page.menu.orphan-routes:Free routes`;
        break;
      }
      case 'map': {
        pageLabel = $localize`:@@subset-page.menu.map:Map`;
        break;
      }
      case 'changes': {
        pageLabel = $localize`:@@subset-page.menu.changes:Changes`;
        break;
      }
    }

    return [
      Breadcrumbs.home,
      Breadcrumbs.analysis,
      { routerLink: routeTypeLink, label: routeTypeLabel },
      { routerLink: countryLink, label: countryLabel },
      { label: pageLabel },
    ];
  });
}
