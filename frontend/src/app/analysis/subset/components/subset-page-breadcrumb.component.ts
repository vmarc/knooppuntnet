import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subset } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { NetworkTypeNameComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-subset-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li>
        <a [routerLink]="networkTypeLink()">
          <kpn-network-type-name [networkType]="subset().networkType" />
        </a>
      </li>
      <li>
        <a [routerLink]="countryLink()">
          <kpn-country-name [country]="subset().country" />
        </a>
      </li>
      <li>
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
      </li>
    </ul>
  `,
  standalone: true,
  imports: [RouterLink, NetworkTypeNameComponent, CountryNameComponent],
})
export class SubsetPageBreadcrumbComponent {
  subset = input.required<Subset>();
  pageName = input.required<string>();

  networkTypeLink() {
    return `/analysis/${this.subset().networkType}`;
  }

  countryLink() {
    return `/analysis/${this.subset().networkType}/${this.subset().country}`;
  }
}
