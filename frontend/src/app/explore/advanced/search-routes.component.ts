import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSearchResult } from '@api/common/search/route-search-result';
import { State } from '@app/state';
import { ListComponent } from '@app/components/shared/list';
import { ListItemComponent } from '@app/components/shared/list';
import { SearchRouteComponent } from './search-route.component';

@Component({
  selector: 'kpn-search-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>{{ rowCount() }} routes</div>
    <kpn-list>
      @for (result of results(); track result.id; let i = $index) {
        <kpn-list-item [selected]="selectedResult() == result" (click)="onSelectionChange(result)">
          <kpn-search-route [routeSearchResult]="result" />
        </kpn-list-item>
      }
    </kpn-list>
  `,
  imports: [
    ListComponent,
    ListItemComponent,
    ListComponent,
    ListItemComponent,
    ListComponent,
    SearchRouteComponent,
  ],
})
export class SearchRoutesComponent {
  private readonly state = inject(State);
  readonly results = computed(() => this.state.explore.routeSearchResults());
  readonly selectedResult = computed(() => this.state.explore.selectedRouteSearchResult());
  readonly rowCount = computed(() => this.results().length);

  onSelectionChange(routeSearchResult: RouteSearchResult): void {
    this.state.explore.updateRouteSearchResult(routeSearchResult);
    console.log(`FOCUS ROUTE ${routeSearchResult.id}`);
  }
}
