import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
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

    <ng-container *ngIf="networkIdSignal() as networkId">
      <ng-container *ngIf="summarySignal() as summary">
        <kpn-page-header [pageTitle]="networkPageTitle(summary.name)" subject="network-page">
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
  standalone: true,
  imports: [
    AsyncPipe,
    MatIconModule,
    NgIf,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
    RouterLink,
  ],
})
export class NetworkPageHeaderComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly networkIdSignal = this.store.selectSignal(selectNetworkId);
  readonly summarySignal = this.store.selectSignal(selectNetworkSummary);

  constructor(private store: Store) {}

  networkPageTitle(networkName: string): string {
    if (networkName) {
      return `${networkName} | ${this.pageTitle}`;
    }
    return null;
  }
}
