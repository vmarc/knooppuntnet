import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSearchResult } from '@api/common/search/route-search-result';
import { DistancePipe } from '@app/components/shared/format';

@Component({
  selector: 'kpn-search-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = routeSearchResult();
    <div class="kpn-line">
      <span>{{ route.scopes[0] }}</span>
      <span>{{ route.id }}</span>
      <span>{{ route.name }}</span>
    </div>
    <div class="kpn-line">
      <span>{{ route.distance | distance }}</span>
    </div>
  `,
  imports: [DistancePipe],
})
export class SearchRouteComponent {
  routeSearchResult = input.required<RouteSearchResult>();
}
