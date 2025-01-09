import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { LocationKey } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { RouteTypeNameComponent } from '@app/components/shared';
import { PageHeaderComponent } from '@app/components/shared/page';

@Component({
  selector: 'kpn-location-selection-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (key(); as key) {
      <kpn-page-header [pageTitle]="'Locations'" subject="network-page">
        <span class="header-network-type-icon">
          <mat-icon [svgIcon]="key.routeType" />
        </span>
        <kpn-network-type-name [routeType]="key.routeType" />
        <span i18n="@@subset.in" class="in">in</span>
        <kpn-country-name [country]="key.country" />
      </kpn-page-header>
    }
  `,
  styles: `
    .in:before {
      content: ' ';
    }

    .in:after {
      content: ' ';
    }
  `,
  imports: [CountryNameComponent, MatIconModule, RouteTypeNameComponent, PageHeaderComponent],
})
export class LocationSelectionPageHeaderComponent {
  key = input.required<LocationKey>();
}
