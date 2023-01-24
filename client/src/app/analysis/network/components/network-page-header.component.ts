import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { selectNetworkSummary } from '../store/network.selectors';
import { selectNetworkId } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.network">Network</li>
    </ul>

    <ng-container *ngIf="networkId$ | async as networkId">
      <ng-container *ngIf="summary$ | async as summary">
        <kpn-page-header
          [pageTitle]="networkPageTitle(summary.name)"
          subject="network-page"
        >
          <span class="header-network-type-icon">
            <mat-icon [svgIcon]="summary.networkType"></mat-icon>
          </span>
          <span>
            {{ summary.name }}
          </span>
        </kpn-page-header>

        <kpn-page-menu>
          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId"
            [active]="pageName === 'details'"
            i18n="@@network-page.menu.details"
          >
            Details
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/facts'"
            [active]="pageName === 'facts'"
            [elementCount]="summary?.factCount"
            i18n="@@network-page.menu.facts"
          >
            Facts
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/nodes'"
            [active]="pageName === 'nodes'"
            [elementCount]="summary?.nodeCount"
            i18n="@@network-page.menu.nodes"
          >
            Nodes
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/routes'"
            [active]="pageName === 'routes'"
            [elementCount]="summary?.routeCount"
            i18n="@@network-page.menu.routes"
          >
            Routes
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/map'"
            [active]="pageName === 'map'"
            i18n="@@network-page.menu.map"
          >
            Map
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/changes'"
            [active]="pageName === 'changes'"
            [elementCount]="summary?.changeCount"
            i18n="@@network-page.menu.changes"
          >
            Changes
          </kpn-page-menu-option>
        </kpn-page-menu>
      </ng-container>
    </ng-container>
  `,
})
export class NetworkPageHeaderComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly networkId$ = this.store.select(selectNetworkId);
  readonly summary$ = this.store.select(selectNetworkSummary);

  constructor(private store: Store) {}

  networkPageTitle(networkName: string): string {
    if (networkName) {
      return `${networkName} | ${this.pageTitle}`;
    }
    return null;
  }
}
