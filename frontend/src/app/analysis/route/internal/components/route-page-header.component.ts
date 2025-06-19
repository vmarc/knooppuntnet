import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { RouteService } from '../route.service';

@Component({
  selector: 'ui-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
    <ui-page-header [pageTitle]="routeDisplayName()" subject="route-page">
      <span>{{ routeDisplayName() }}</span>
    </ui-page-header>
    <ui-page-menu [pageName]="pageName()" [options]="menuOptions()" />
  `,
  imports: [PageHeaderComponent, PageMenuComponent, BreadcrumbComponent],
})
export class RoutePageHeaderComponent {
  readonly pageName = input.required<string>();
  private readonly service = inject(RouteService);
  protected readonly routeDisplayName = this.service.routeDisplayName;

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.routeLabel },
  ];

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    const link = `/analysis/route/${this.service.routeId()}`;
    return [
      {
        pageName: 'details',
        pageLink: link,
        label: $localize`:@@route.menu.details:Details`,
      },
      {
        pageName: 'map',
        pageLink: link + '/map',
        label: $localize`:@@route.menu.map:Map`,
      },
      {
        pageName: 'changes',
        pageLink: link + '/changes',
        label: $localize`:@@route.menu.changes:Changes`,
        elementCount: this.service.changeCount(),
      },
      {
        pageName: 'segments',
        pageLink: link + '/segments',
        label: $localize`:@@route.menu.segments:Segments`,
        elementCount: this.service.segmentCount(),
      },
      {
        pageName: 'paths',
        pageLink: link + '/paths',
        label: $localize`:@@route.menu.paths:Paths`,
        elementCount: 999, // TODO redesign - this.service.pathCount(),
      },
    ];
  });
}
