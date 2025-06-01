import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationKey } from '@api/custom/location-key';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { Util } from '@app/shared/components/util';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'ui-location-selection-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (key(); as key) {
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
    }
  `,
  imports: [BreadcrumbComponent],
})
export class LocationSelectionPageBreadcrumbComponent {
  readonly key = input.required<LocationKey>();

  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    const key = this.key();
    const routeTypeLink = `/analysis/${key.routeType}`;
    const routeTypeLabel = Translations.get('route-type.' + key.routeType);
    const countryLink = `/analysis/${key.routeType}/key.country`;
    const countryLabel = Translations.get('country.' + Util.safeGet(() => key.country));
    return [
      Breadcrumbs.home,
      Breadcrumbs.analysis,
      { routerLink: routeTypeLink, label: routeTypeLabel },
      { routerLink: countryLink, label: countryLabel },
    ];
  });
}
