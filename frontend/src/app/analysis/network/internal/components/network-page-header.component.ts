import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NetworkService } from '../network.service';

@Component({
  selector: 'kpn-network-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <span i18n="@@breadcrumb.network">Network</span>
      </nz-breadcrumb-item>
    </nz-breadcrumb>

    @if (service.networkId(); as networkId) {
      @if (service.summary(); as summary) {
        <kpn-page-header [pageTitle]="networkPageTitle(summary.name)" subject="network-page">
          <span class="header-route-type-icon">
            <nz-icon [nzType]="summary.routeType" />
          </span>
          <span>
            {{ summary.name }}
          </span>
        </kpn-page-header>

        <kpn-page-menu>
          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId"
            [active]="pageName() === 'details'"
            i18n="@@network-page.menu.details"
          >
            Details
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/facts'"
            [active]="pageName() === 'facts'"
            [elementCount]="summary?.factCount"
            i18n="@@network-page.menu.facts"
          >
            Facts
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/nodes'"
            [active]="pageName() === 'nodes'"
            [elementCount]="summary?.nodeCount"
            i18n="@@network-page.menu.nodes"
          >
            Nodes
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/routes'"
            [active]="pageName() === 'routes'"
            [elementCount]="summary?.routeCount"
            i18n="@@network-page.menu.routes"
          >
            Routes
          </kpn-page-menu-option>

          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/map'"
            [active]="pageName() === 'map'"
            i18n="@@network-page.menu.map"
          >
            Map
          </kpn-page-menu-option>

          <!-- [elementCount]="summary?.changeCount"-->
          <kpn-page-menu-option
            [link]="'/analysis/network/' + networkId + '/changes'"
            [active]="pageName() === 'changes'"
            [elementCount]="999"
            i18n="@@network-page.menu.changes"
          >
            Changes
          </kpn-page-menu-option>
        </kpn-page-menu>
      }
    }
  `,
  imports: [
    MatIconModule,
    NzIconDirective,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
    RouterLink,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
  ],
})
export class NetworkPageHeaderComponent {
  pageName = input.required<string>();
  pageTitle = input.required<string>();

  protected readonly service = inject(NetworkService);

  networkPageTitle(networkName: string): string {
    if (networkName) {
      return `${networkName} | ${this.pageTitle()}`;
    }
    return null;
  }
}
