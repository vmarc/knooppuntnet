import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationKey } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { NetworkTypeNameComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-location-selection-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (key(); as key) {
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li>
          <a [routerLink]="'/analysis/' + key.networkType">
            <kpn-network-type-name [networkType]="key.networkType" />
          </a>
        </li>
        <li>
          <kpn-country-name [country]="key.country" />
        </li>
      </ul>
    }
  `,
  standalone: true,
  imports: [CountryNameComponent, NetworkTypeNameComponent, RouterLink],
})
export class LocationSelectionPageBreadcrumbComponent {
  key = input.required<LocationKey>();
}
