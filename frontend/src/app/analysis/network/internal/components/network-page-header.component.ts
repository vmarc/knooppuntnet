import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NetworkService } from '../network.service';

@Component({
  selector: 'ui-network-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />

    @if (service.networkId(); as networkId) {
      @if (service.summary(); as summary) {
        <ui-page-header [pageTitle]="networkPageTitle(summary.name)" subject="network-page">
          <span class="header-route-type-icon">
            <nz-icon [nzType]="summary.routeType" />
          </span>
          <span>
            {{ summary.name }}
          </span>
        </ui-page-header>

        <ui-page-menu>
          <ui-page-menu-option
            [link]="'/analysis/network/' + networkId"
            [active]="pageName() === 'details'"
            i18n="@@network-page.menu.details"
          >
            Details
          </ui-page-menu-option>

          <ui-page-menu-option
            [link]="'/analysis/network/' + networkId + '/facts'"
            [active]="pageName() === 'facts'"
            [elementCount]="summary?.factCount"
            i18n="@@network-page.menu.facts"
          >
            Facts
          </ui-page-menu-option>

          <ui-page-menu-option
            [link]="'/analysis/network/' + networkId + '/nodes'"
            [active]="pageName() === 'nodes'"
            [elementCount]="summary?.nodeCount"
            i18n="@@network-page.menu.nodes"
          >
            Nodes
          </ui-page-menu-option>

          <ui-page-menu-option
            [link]="'/analysis/network/' + networkId + '/routes'"
            [active]="pageName() === 'routes'"
            [elementCount]="summary?.routeCount"
            i18n="@@network-page.menu.routes"
          >
            Routes
          </ui-page-menu-option>

          <ui-page-menu-option
            [link]="'/analysis/network/' + networkId + '/map'"
            [active]="pageName() === 'map'"
            i18n="@@network-page.menu.map"
          >
            Map
          </ui-page-menu-option>

          <!-- [elementCount]="summary?.changeCount"-->
          <ui-page-menu-option
            [link]="'/analysis/network/' + networkId + '/changes'"
            [active]="pageName() === 'changes'"
            [elementCount]="999"
            i18n="@@network-page.menu.changes"
          >
            Changes
          </ui-page-menu-option>
        </ui-page-menu>
      }
    }
  `,
  imports: [
    MatIconModule,
    NzIconDirective,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
    BreadcrumbComponent,
  ],
})
export class NetworkPageHeaderComponent {
  pageName = input.required<string>();
  pageTitle = input.required<string>();

  protected readonly service = inject(NetworkService);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.networkLabel },
  ];

  networkPageTitle(networkName: string): string {
    if (networkName) {
      return `${networkName} | ${this.pageTitle()}`;
    }
    return null;
  }
}
