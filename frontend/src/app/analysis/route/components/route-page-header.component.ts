import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';
import { RouteService } from '../route.service';

@Component({
  selector: 'kpn-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header [pageTitle]="service.routeName()" subject="route-page">
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="service.networkType()" />
      </span>
      <span i18n="@@route.title">Route</span>
      @if (service.routeName()) {
        <span>&nbsp;{{ service.routeName() }}</span>
      } @else {
        <span>&nbsp;{{ service.routeId() }}</span>
      }
    </kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="linkRouteDetails()"
        [active]="pageName() === 'details'"
        i18n="@@route.menu.details"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteMap()"
        [active]="pageName() === 'map'"
        i18n="@@route.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteChanges()"
        [active]="pageName() === 'changes'"
        [elementCount]="service.changeCount()"
        i18n="@@route.menu.changes"
      >
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
  standalone: true,
  imports: [MatIconModule, PageHeaderComponent, PageMenuComponent, PageMenuOptionComponent],
})
export class RoutePageHeaderComponent {
  pageName = input.required<string>();

  protected readonly service = inject(RouteService);

  linkRouteDetails(): string {
    return this.linkRoute('');
  }

  linkRouteMap(): string {
    return this.linkRoute('/map');
  }

  linkRouteChanges(): string {
    return this.linkRoute('/changes');
  }

  private linkRoute(suffix: string): string {
    return `/analysis/route/${this.service.routeId()}${suffix}`;
  }
}
