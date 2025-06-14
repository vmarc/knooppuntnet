import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { RouteType } from '@api/common/route-type';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';
import { NetworkRouteAnalysisComponent } from './network-route-analysis.component';

@Component({
  selector: 'ui-network-route-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = row();
    <div class="kpn-line">
      <span>{{ rowNumber() }}</span>
      <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
      <ui-link-route [routeId]="route.id" [routeName]="route.name" [routeType]="routeType()" />
      <span>
        <span i18n="@@network-routes.table.distance" class="kpn-label">Distance</span>
        <span>{{ route.length | distance }}</span>
      </span>
    </div>
    <div class="kpn-line">
      <ui-network-route-analysis [route]="route" [routeType]="routeType()" />
      @if (route.symbol) {
        <ui-symbol [description]="route.symbol" [width]="25" [height]="25" />
      }

      @if (route.lastSurvey) {
        <span>
          <span i18n="@@network-nodes.table.last-survey" class="kpn-label">Survey</span>
          <span>
            {{ route.lastSurvey | day }}
          </span>
        </span>
      }

      <span>
        <span i18n="@@network-routes.table.last-edit" class="kpn-label">Last edit</span>
        <ui-day [timestamp]="route.lastUpdated" />
      </span>
    </div>

    @if (route.role) {
      <div class="kpn-line">
        <span i18n="@@network-routes.table.role" class="kpn-label">Role</span>
        {{ route.role }}
      </div>
    }
  `,
  imports: [
    ActionButtonRouteComponent,
    DayComponent,
    DayPipe,
    DistancePipe,
    LinkRouteComponent,
    NetworkRouteAnalysisComponent,
    SymbolComponent,
  ],
})
export class NetworkRouteListItemComponent {
  readonly routeType = input.required<RouteType>();
  readonly rowNumber = input.required<number>();
  readonly row = input.required<NetworkRouteRow>();
}
