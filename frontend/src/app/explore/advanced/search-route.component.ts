import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NetworkType } from '@api/common';
import { RouteSearchResult } from '@api/common/search/route-search-result';
import { DistancePipe } from '@app/components/shared/format';
import { ActionButtonRouteComponent } from '../../analysis/components/action/action-button-route.component';
import { ScopeIconComponent } from '../scope-icon.component';

@Component({
  selector: 'kpn-search-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = routeSearchResult();
    <div class="kpn-line">
      <kpn-scope-icon [scope]="route.scopes[0]" />
      <kpn-action-button-route [networkType]="networkType()" [relationId]="route.id" />
      <a [routerLink]="'/analysis/route/' + route.id">{{ route.name }}</a>
    </div>
    <div class="kpn-line">
      <span>{{ route.distance | distance }}</span>
    </div>
  `,
  imports: [DistancePipe, ScopeIconComponent, RouterLink, ActionButtonRouteComponent],
})
export class SearchRouteComponent {
  networkType = input.required<NetworkType>();
  routeSearchResult = input.required<RouteSearchResult>();
}
