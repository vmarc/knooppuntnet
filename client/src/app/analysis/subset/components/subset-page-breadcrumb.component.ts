import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Subset } from '@api/custom';

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
          <kpn-network-type-name [networkType]="subset.networkType" />
        </a>
      </li>
      <li>
        <a [routerLink]="countryLink()">
          <kpn-country-name [country]="subset.country" />
        </a>
      </li>
      <li>
        <span
          *ngIf="pageName === 'networks'"
          i18n="@@subset-page.menu.networks"
        >
          Networks
        </span>
        <span *ngIf="pageName === 'facts'" i18n="@@subset-page.menu.facts">
          Facts
        </span>
        <span
          *ngIf="pageName === 'orphan-nodes'"
          i18n="@@subset-page.menu.orphan-nodes"
        >
          Orphan nodes
        </span>
        <span
          *ngIf="pageName === 'orphan-routes'"
          i18n="@@subset-page.menu.orphan-routes"
        >
          Free routes
        </span>
        <span *ngIf="pageName === 'map'" i18n="@@subset-page.menu.map">
          Map
        </span>
        <span *ngIf="pageName === 'changes'" i18n="@@subset-page.menu.changes">
          Changes
        </span>
      </li>
    </ul>
  `,
})
export class SubsetPageBreadcrumbComponent {
  @Input() subset: Subset;
  @Input() pageName: string;

  networkTypeLink() {
    return `/analysis/${this.subset.networkType}`;
  }

  countryLink() {
    return `/analysis/${this.subset.networkType}/${this.subset.country}`;
  }
}
