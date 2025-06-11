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
    const countryLabel = Translations.get('country.' + Util.safeGet(() => subset.country));

    return [
      Breadcrumbs.home,
      Breadcrumbs.analysis,
      { routerLink: routeTypeLink, label: routeTypeLabel },
      { label: countryLabel },
    ];
  });
}
