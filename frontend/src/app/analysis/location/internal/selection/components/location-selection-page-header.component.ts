import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationKey } from '@api/custom/location-key';
import { CountryNameComponent } from '@app/shared/components/country-name.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { RouteTypeNameComponent } from '@app/shared/components/route-type-name.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-location-selection-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (key(); as key) {
      <ui-page-header pageTitle="Locations" subject="network-page">
        <span class="header-route-type-icon">
          <nz-icon [nzType]="key.routeType" />
        </span>
        <ui-route-type-name [routeType]="key.routeType" />
        <span i18n="@@subset.in" class="in">in</span>
        <ui-country-name [country]="key.country" />
      </ui-page-header>
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
  imports: [CountryNameComponent, NzIconDirective, PageHeaderComponent, RouteTypeNameComponent],
})
export class LocationSelectionPageHeaderComponent {
  readonly key = input.required<LocationKey>();
}
