import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzNoAnimationDirective } from 'ng-zorro-antd/core/no-animation';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';
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

    <div class="kpn-small-spacer-above kpn-small-spacer-below buttons">
      <button nz-button nz-tooltip nzShape="circle" nzNoAnimation>
        <nz-icon nzType="ellipsis" />
      </button>
      <button
        nz-button
        nz-tooltip
        nzShape="circle"
        nzNoAnimation
        (click)="zoomToFitRoute()"
        nzTooltipTitle="zoom to fit route on map"
        nzTooltipPlacement="right"
      >
        <nz-icon nzType="arrows-alt" />
      </button>
    </div>
  `,
  styles: `
    .buttons {
      display: flex;
      gap: 0.5em;
    }
  `,
  imports: [
    BreadcrumbComponent,
    NzButtonComponent,
    NzIconDirective,
    NzNoAnimationDirective,
    NzTooltipDirective,
    PageHeaderComponent,
    PageMenuComponent,
  ],
})
export class RoutePageHeaderComponent {
  private readonly service = inject(RouteService);
  protected pageName = this.service.pageName;
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
        pageName: 'members',
        pageLink: link + '/members',
        label: $localize`:@@route.menu.members:Members`,
        elementCount: 1,
      },
      {
        pageName: 'paths',
        pageLink: link + '/paths',
        label: $localize`:@@route.menu.paths:Paths`,
        elementCount: 2,
      },
      {
        pageName: 'segments',
        pageLink: link + '/segments',
        label: $localize`:@@route.menu.segments:Segments`,
        elementCount: 3,
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
    ];
  });

  zoomToFitRoute() {
    // TODO redesign
  }
}
