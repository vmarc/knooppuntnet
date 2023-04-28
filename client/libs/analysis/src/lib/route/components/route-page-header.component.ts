import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/custom';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';

@Component({
  selector: 'kpn-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header [pageTitle]="routeName" subject="route-page">
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="networkType" />
      </span>
      <span i18n="@@route.title">Route</span>
      <span *ngIf="routeName">&nbsp;{{ routeName }}</span>
      <span *ngIf="!routeName">&nbsp;{{ routeId }}</span>
    </kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="linkRouteDetails()"
        [active]="pageName === 'details'"
        i18n="@@route.menu.details"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteMap()"
        [active]="pageName === 'map'"
        i18n="@@route.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteChanges()"
        [active]="pageName === 'changes'"
        [elementCount]="changeCount"
        i18n="@@route.menu.changes"
      >
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
  standalone: true,
  imports: [
    PageHeaderComponent,
    MatIconModule,
    NgIf,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
})
export class RoutePageHeaderComponent {
  @Input() routeId: string;
  @Input() routeName: string;
  @Input() pageName: string;
  @Input() changeCount: number;
  @Input() networkType: NetworkType;

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
    return `/analysis/route/${this.routeId}${suffix}`;
  }
}
