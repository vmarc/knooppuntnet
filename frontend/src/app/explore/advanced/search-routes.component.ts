import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteListItem } from '@api/common/search/route-list-item';
import { State } from '@app/state';
import { ListComponent } from '@app/components/shared/list';
import { ListItemComponent } from '@app/components/shared/list';
import { MapService } from '../../map/map.service';
import { ScopeIconComponent } from '../scope-icon.component';
import { SearchRouteComponent } from './search-route.component';

@Component({
  selector: 'kpn-search-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>{{ rowCount() }} routes</div>
    @let list = routeList();
    @if (list?.international) {
      <div class="kpn-line">
        <kpn-scope-icon scope="international" />
        <span>International</span>
      </div>
      <kpn-list>
        @for (item of list.international; track item.id; let i = $index) {
          <kpn-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <kpn-search-route [networkType]="networkType()" [item]="item" />
          </kpn-list-item>
        }
      </kpn-list>
    }

    @if (list?.national) {
      <div class="kpn-line">
        <kpn-scope-icon scope="national" />
        <span>National</span>
      </div>
      <kpn-list>
        @for (item of list.national; track item.id; let i = $index) {
          <kpn-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <kpn-search-route [networkType]="networkType()" [item]="item" />
          </kpn-list-item>
        }
      </kpn-list>
    }

    @if (list?.regional) {
      <div class="kpn-line">
        <kpn-scope-icon scope="regional" />
        <span>Regional</span>
      </div>
      <kpn-list>
        @for (item of list.regional; track item.id; let i = $index) {
          <kpn-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <kpn-search-route [networkType]="networkType()" [item]="item" />
          </kpn-list-item>
        }
      </kpn-list>
    }

    @if (list?.local) {
      <div class="kpn-line">
        <kpn-scope-icon scope="local" />
        <span>Local</span>
      </div>
      <kpn-list>
        @for (item of list.local; track item.id; let i = $index) {
          <kpn-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <kpn-search-route [networkType]="networkType()" [item]="item" />
          </kpn-list-item>
        }
      </kpn-list>
    }

    @if (list?.unknown) {
      <div class="kpn-line">
        <kpn-scope-icon scope="unknown" />
        <span>Other</span>
      </div>
      <kpn-list>
        @for (item of list.unknown; track item.id; let i = $index) {
          <kpn-list-item [selected]="selectedResult() == item" (click)="onSelectionChange(item)">
            <kpn-search-route [networkType]="networkType()" [item]="item" />
          </kpn-list-item>
        }
      </kpn-list>
    }
  `,
  imports: [ListComponent, ListItemComponent, SearchRouteComponent, ScopeIconComponent],
})
export class SearchRoutesComponent {
  private readonly state = inject(State);
  private readonly mapService = inject(MapService);
  readonly networkType = computed(() => this.state.page.networkType());
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
