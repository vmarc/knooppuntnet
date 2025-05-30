import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteListItem } from '@api/common/search/route-list-item';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { State } from '@app/state/state';
import { MapService } from '@app/map/map.service';
import { ScopeIconComponent } from '../scope-icon.component';
import { SearchRouteComponent } from './search-route.component';

@Component({
  selector: 'ui-search-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>{{ rowCount() }} routes</div>
    @let list = routeList();
    @if (list?.international) {
      <div class="kpn-line">
        <ui-scope-icon scope="international" />
        <span>International</span>
      </div>
      <ui-list>
        @for (item of list.international; track item.id; let i = $index) {
          <ui-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <ui-search-route [routeType]="routeType()" [item]="item" />
          </ui-list-item>
        }
      </ui-list>
    }

    @if (list?.national) {
      <div class="kpn-line">
        <ui-scope-icon scope="national" />
        <span>National</span>
      </div>
      <ui-list>
        @for (item of list.national; track item.id; let i = $index) {
          <ui-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <ui-search-route [routeType]="routeType()" [item]="item" />
          </ui-list-item>
        }
      </ui-list>
    }

    @if (list?.regional) {
      <div class="kpn-line">
        <ui-scope-icon scope="regional" />
        <span>Regional</span>
      </div>
      <ui-list>
        @for (item of list.regional; track item.id; let i = $index) {
          <ui-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <ui-search-route [routeType]="routeType()" [item]="item" />
          </ui-list-item>
        }
      </ui-list>
    }

    @if (list?.local) {
      <div class="kpn-line">
        <ui-scope-icon scope="local" />
        <span>Local</span>
      </div>
      <ui-list>
        @for (item of list.local; track item.id; let i = $index) {
          <ui-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <ui-search-route [routeType]="routeType()" [item]="item" />
          </ui-list-item>
        }
      </ui-list>
    }

    @if (list?.unknown) {
      <div class="kpn-line">
        <ui-scope-icon scope="unknown" />
        <span>Other</span>
      </div>
      <ui-list>
        @for (item of list.unknown; track item.id; let i = $index) {
          <ui-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <ui-search-route [routeType]="routeType()" [item]="item" />
          </ui-list-item>
        }
      </ui-list>
    }
  `,
  imports: [ListComponent, ListItemComponent, SearchRouteComponent, ScopeIconComponent],
})
export class SearchRoutesComponent {
  private readonly state = inject(State);
  private readonly mapService = inject(MapService);
  readonly routeType = computed(() => this.state.page.routeType());
  readonly routeList = computed(() => this.state.explore.routeList());
  readonly selectedResult = computed(() => this.state.explore.selectedRouteListItem());
  readonly rowCount = computed(() => (this.routeList() ? this.routeList().size : 0));

  onSelectionChange(item: RouteListItem): void {
    this.state.explore.updateRouteListItem(item);
    this.mapService.focusElements(item.bounds, {
      nodeIds: [],
      routeIds: item.routeIds.map((id) => id.toString()),
    });
  }
}
