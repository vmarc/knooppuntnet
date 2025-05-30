import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subset } from '@api/custom/subset';
import { CountryNameComponent } from '@app/shared/components/country-name.component';
import { RouteTypeNameComponent } from '@app/shared/components/route-type-name.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';

@Component({
  selector: 'ui-subset-page-breadcrumb',
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
          <ui-route-type-name [routeType]="subset().routeType" />
        </a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a [routerLink]="countryLink()">
          <ui-country-name [country]="subset().country" />
        </a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        @switch (pageName()) {
          @case ('networks') {
            <span i18n="@@subset-page.menu.networks">Networks</span>
          }
          @case ('facts') {
            <span i18n="@@subset-page.menu.facts">Facts</span>
          }
          @case ('orphan-nodes') {
            <span i18n="@@subset-page.menu.orphan-nodes">Orphan nodes</span>
          }
          @case ('orphan-routes') {
            <span i18n="@@subset-page.menu.orphan-routes">Free routes</span>
          }
          @case ('map') {
            <span i18n="@@subset-page.menu.map">Map</span>
          }
          @case ('changes') {
            <span i18n="@@subset-page.menu.changes">Changes</span>
          }
        }
      </nz-breadcrumb-item>
    </nz-breadcrumb>
  `,
  imports: [
    CountryNameComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    RouteTypeNameComponent,
    RouterLink,
  ],
})
export class SubsetPageBreadcrumbComponent {
  subset = input.required<Subset>();
  pageName = input.required<string>();

  routeTypeLink() {
    return `/analysis/${this.subset().routeType}`;
  }

  countryLink() {
    return `/analysis/${this.subset().routeType}/${this.subset().country}/networks`;
  }
}
