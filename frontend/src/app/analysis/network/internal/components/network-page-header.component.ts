import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkPage } from '@app/analysis/network/internal/components/network-page';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NetworkService } from '../network.service';

interface MenuOption {
  pageName: NetworkPage;
  pageLink: string;
  label: string;
  elementCount?: number;
}

@Component({
  selector: 'ui-network-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />

    @if (summary(); as summary) {
      <ui-page-header [pageTitle]="networkPageTitle()" subject="network-page">
        <span class="header-route-type-icon">
          <nz-icon [nzType]="summary.routeType" />
        </span>
        <span>
          {{ summary.name }}
        </span>
      </ui-page-header>

      <ui-page-menu>
        @for (option of menuOptions(); track option.pageName) {
          <ui-page-menu-option
            [link]="option.pageLink"
            [active]="pageName() === option.pageName"
            [elementCount]="option.elementCount"
            >{{ option.label }}
          </ui-page-menu-option>
        }
      </ui-page-menu>
    }
  `,
  imports: [
    BreadcrumbComponent,
    NzIconDirective,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
})
export class NetworkPageHeaderComponent {
  readonly pageName = input.required<NetworkPage>();
  readonly pageTitle = input.required<string>();

  private readonly service = inject(NetworkService);
  protected readonly summary = this.service.summary;

  protected readonly networkPageTitle = computed(() => {
    const name = this.summary()?.name;
    return name ? name : this.pageTitle();
  });

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.networkLabel },
  ];

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    const summary = this.service.summary();
    const link = '/analysis/network/' + this.service.networkId();
    return [
      {
        pageName: 'details',
        pageLink: link,
        label: $localize`:@@network-page.menu.details:Details`,
      },
      {
        pageName: 'facts',
        pageLink: link + '/facts',
        label: $localize`:@@network-page.menu.facts:Facts`,
        elementCount: summary?.factCount,
      },
      {
        pageName: 'nodes',
        pageLink: link + '/nodes',
        label: $localize`:@@network-page.menu.nodes:Nodes`,
        elementCount: summary?.nodeCount,
      },
      {
        pageName: 'routes',
        pageLink: link + '/routes',
        label: $localize`:@@network-page.menu.routes:Routes`,
        elementCount: summary?.routeCount,
      },
      {
        pageName: 'map',
        pageLink: link + '/map',
        label: $localize`:@@network-page.menu:Map`,
      },
      {
        pageName: 'changes',
        pageLink: link + '/changes',
        label: $localize`:@@network-page.menu.changes:Changes`,
        elementCount: 999,
      },
    ];
  });
}
