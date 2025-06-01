import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationKey } from '@api/custom/location-key';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { LocationPipe } from '@app/shared/components/format/location.pipe';
import { Util } from '@app/shared/components/util';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'ui-location-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" /> `,
  imports: [BreadcrumbComponent],
})
export class LocationPageBreadcrumbComponent {
  readonly locationKey = input.required<LocationKey>();

  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    const key = this.locationKey();
    const routeTypeLink = `/analysis/${key.routeType}`;
    const routeTypeLabel = Translations.get('route-type.' + key.routeType);
    const countryLink = `/analysis/${key.routeType}/key.country`;
    const countryLabel = Translations.get('country.' + Util.safeGet(() => key.country));
    const nameParts = key.name.split(':');
    const name = nameParts[nameParts.length - 1];
    const locationName = new LocationPipe().transform(name);
    return [
      Breadcrumbs.home,
      Breadcrumbs.analysis,
      { routerLink: routeTypeLink, label: routeTypeLabel },
      { routerLink: countryLink, label: countryLabel },
      { label: locationName },
    ];
  });
}
