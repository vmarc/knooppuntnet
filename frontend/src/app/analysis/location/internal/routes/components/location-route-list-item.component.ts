import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { FactsLineComponent } from '@app/analysis/fact/components/facts-line.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';

@Component({
  selector: 'ui-location-route-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = item();
    <div class="kpn-line">
      <span>{{ route.rowIndex + 1 }}</span>
      @if (route.symbol) {
        <ui-symbol [description]="route.symbol" [width]="25" [height]="25" />
      }
      <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
      <ui-link-route [routeId]="route.id" [routeName]="route.name" [routeType]="routeType()" />
      <span>{{ route.meters | integer }} m</span>
    </div>
    <div class="kpn-line">
      <span>
        <span i18n="@@location-routes.table.last-edit" class="kpn-label">Last edit</span>
        <ui-day [timestamp]="route.lastUpdated" />
      </span>

      @if (route.lastSurvey) {
        <span i18n="@@location-routes.table.last-survey" class="kpn-label">Survey</span>
        <span>{{ route.lastSurvey | day }}</span>
      }
    </div>
    @if (route.facts) {
      <ui-facts-line [facts]="route.facts" />
    }
  `,
  imports: [
    ActionButtonRouteComponent,
    DayComponent,
    DayPipe,
    IntegerFormatPipe,
    LinkRouteComponent,
    SymbolComponent,
    FactsLineComponent,
  ],
})
export class LocationRouteListItemComponent {
  readonly routeType = input.required<RouteType>();
  readonly item = input.required<LocationRouteInfo>();
}
