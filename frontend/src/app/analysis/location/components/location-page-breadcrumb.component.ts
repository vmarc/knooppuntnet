import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationKey } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { RouteTypeNameComponent } from '@app/components/shared';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { LocationPipe } from '../../../shared/components/shared/format/location.pipe';

@Component({
  selector: 'kpn-location-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a [routerLink]="routeTypeLink()">
          <kpn-route-type-name [routeType]="locationKey().routeType" />
        </a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a [routerLink]="countryLink()">
          <kpn-country-name [country]="locationKey().country" />
        </a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        {{ locationName() | location }}
      </nz-breadcrumb-item>
    </nz-breadcrumb>
  `,
  imports: [
    CountryNameComponent,
    LocationPipe,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    RouteTypeNameComponent,
    RouterLink,
  ],
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
