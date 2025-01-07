import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NetworkType } from '@api/common';
import { RouteListItem } from '@api/common/search/route-list-item';
import { DistancePipe } from '@app/components/shared/format';
import { ActionButtonRouteComponent } from '../../analysis/components/action/action-button-route.component';

@Component({
  selector: 'kpn-search-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = item();
    <div class="kpn-line">
      <kpn-action-button-route [networkType]="networkType()" [relationId]="route.id" />
      <a [routerLink]="'/analysis/route/' + route.id">{{ route.name }}</a>
    </div>
    <div class="kpn-line">
      <span>{{ route.distance | distance }}</span>
    </div>
  `,
  imports: [DistancePipe, RouterLink, ActionButtonRouteComponent],
})
export class SearchRouteComponent {
  networkType = input.required<NetworkType>();
  item = input.required<RouteListItem>();
}
