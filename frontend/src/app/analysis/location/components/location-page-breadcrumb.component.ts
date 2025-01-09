import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationKey } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { RouteTypeNameComponent } from '@app/components/shared';
import { LocationPipe } from '../../../shared/components/shared/format/location.pipe';

@Component({
  selector: 'kpn-location-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li>
        <a [routerLink]="routeTypeLink()">
          <kpn-route-type-name [routeType]="locationKey().routeType" />
        </a>
      </li>
      <li>
        <a [routerLink]="countryLink()">
          <kpn-country-name [country]="locationKey().country" />
        </a>
      </li>
      <li>{{ locationName() | location }}</li>
    </ul>
  `,
  imports: [RouterLink, RouteTypeNameComponent, CountryNameComponent, LocationPipe],
})
export class LocationPageBreadcrumbComponent {
  locationKey = input.required<LocationKey>();

  routeTypeLink(): string {
    return `/analysis/${this.locationKey().routeType}`;
  }

  countryLink(): string {
    return `/analysis/${this.locationKey().routeType}/${this.locationKey().country}`;
  }

  locationName(): string {
    const nameParts = this.locationKey().name.split(':');
    return nameParts[nameParts.length - 1];
  }
}
