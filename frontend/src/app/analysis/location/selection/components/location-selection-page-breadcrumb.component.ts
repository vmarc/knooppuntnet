import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationKey } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { RouteTypeNameComponent } from '@app/components/shared';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';

@Component({
  selector: 'kpn-location-selection-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (key(); as key) {
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a [routerLink]="'/analysis/' + key.routeType">
            <kpn-route-type-name [routeType]="key.routeType" />
          </a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <kpn-country-name [country]="key.country" />
        </nz-breadcrumb-item>
      </nz-breadcrumb>
    }
  `,
  imports: [
    CountryNameComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    RouteTypeNameComponent,
    RouterLink,
  ],
})
export class LocationSelectionPageBreadcrumbComponent {
  key = input.required<LocationKey>();
}
