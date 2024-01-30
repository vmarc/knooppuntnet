import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/custom';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';

@Component({
  selector: 'kpn-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header [pageTitle]="routeName()" subject="route-page">
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="networkType()" />
      </span>
      <span i18n="@@route.title">Route</span>
      @if (routeName()) {
        <span>&nbsp;{{ routeName() }}</span>
      } @else {
        <span>&nbsp;{{ routeId() }}</span>
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
        [elementCount]="changeCount()"
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
  routeId = input.required<string>();
  routeName = input.required<string>();
  pageName = input.required<string>();
  changeCount = input.required<number>();
  networkType = input.required<NetworkType>();

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
    return `/analysis/route/${this.routeId()}${suffix}`;
  }
}
