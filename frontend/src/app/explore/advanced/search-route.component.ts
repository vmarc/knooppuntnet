import { computed, input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteType } from '@api/common/route-type';
import { RouteListItem } from '@api/common/search/route-list-item';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { ActionButtonRouteComponent } from '@app/analysis/components/action/action-button-route.component';

@Component({
  selector: 'ui-search-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = item();
    <div class="kpn-line">
      <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
      <a [routerLink]="routeLink()">{{ route.name }}</a>
    </div>
    <div class="kpn-line">
      <span>{{ route.distance | distance }}</span>
    </div>
  `,
  imports: [DistancePipe, RouterLink, ActionButtonRouteComponent, DistancePipe],
})
export class SearchRouteComponent {
  readonly routeType = input.required<RouteType>();
  readonly item = input.required<RouteListItem>();
  readonly routeLink = computed(() => `/analysis/route/${this.item().id}`);
}
