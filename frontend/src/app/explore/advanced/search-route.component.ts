import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteType } from '@api/common/route-type';
import { RouteListItem } from '@api/common/search/route-list-item';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { ActionButtonRouteComponent } from '@app/analysis/components/action/action-button-route.component';

@Component({
  selector: 'kpn-search-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = item();
    <div class="kpn-line">
      <kpn-action-button-route [routeType]="routeType()" [relationId]="route.id" />
      <a [routerLink]="'/analysis/route/' + route.id">{{ route.name }}</a>
    </div>
    <div class="kpn-line">
      <span>{{ route.distance | distance }}</span>
    </div>
  `,
  imports: [DistancePipe, RouterLink, ActionButtonRouteComponent, DistancePipe],
})
export class SearchRouteComponent {
  routeType = input.required<RouteType>();
  item = input.required<RouteListItem>();
}
