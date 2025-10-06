import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { RouteScope } from '@api/common/route-scope';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { LocationNodeAnalysisComponent } from './location-node-analysis.component';
import { LocationNodeRoutesComponent } from './location-node-routes.component';

@Component({
  selector: 'ui-location-node-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <span>{{ node().rowIndex + 1 }}</span>
      <ui-action-button-node [nodeId]="node().id" />
      <ui-link-node [nodeId]="node().id" [nodeName]="node().name" />
      <span>{{ node().longName }}</span>
    </div>
    <div class="kpn-line">
      <span>
        <span i18n="@@location-nodes.table.last-edit" class="kpn-label">Last edit</span>
        <span>
          <ui-day [timestamp]="node().lastUpdated" />
        </span>
      </span>
      @if (node().lastSurvey) {
        <span>
          <span i18n="@@location-nodes.table.last-survey" class="kpn-label">Survey</span>
          <span>
            {{ node().lastSurvey | day }}
          </span>
        </span>
      }
    </div>
    <ui-location-node-analysis
      [node]="node()"
      [routeType]="routeType()"
      [routeScope]="routeScope()"
    />
    <ui-location-node-routes [node]="node()" />
  `,
  imports: [
    ActionButtonNodeComponent,
    DayComponent,
    DayPipe,
    LinkNodeComponent,
    LocationNodeAnalysisComponent,
    LocationNodeRoutesComponent,
  ],
})
export class LocationNodeListItemComponent {
  readonly routeType = input.required<RouteType>();
  readonly routeScope = input.required<RouteScope>();
  readonly node = input.required<LocationNodeInfo>();
}
