import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { NetworkNodeAnalysisComponent } from './network-node-analysis.component';
import { NetworkNodeRoutesComponent } from './network-node-routes.component';

@Component({
  selector: 'ui-network-node-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let node = row();
    <div class="kpn-line">
      <span>{{ rowNumber() }}</span>
      <ui-action-button-node [nodeId]="node.detail.id" />
      <ui-link-node [nodeId]="node.detail.id" [nodeName]="node.detail.name" />
      <span>{{ node.detail.longName }}</span>
    </div>
    @if (node.detail.lastSurvey) {
      <div class="kpn-line">
        <span i18n="@@network-nodes.table.last-survey" class="kpn-label">Survey</span>
        <span>
          {{ node.detail.lastSurvey | day }}
        </span>
      </div>
    }
    <div class="kpn-line">
      <span i18n="@@network-nodes.table.last-edit" class="kpn-label">Last edit</span>
      <span>
        <ui-day [timestamp]="node.detail.timestamp" />
      </span>
    </div>
    <ui-network-node-analysis [routeType]="routeType()" [routeScope]="routeScope()" [node]="node" />
    <div>
      <ui-network-node-routes [node]="node" />
    </div>
  `,
  imports: [
    ActionButtonNodeComponent,
    DayComponent,
    DayPipe,
    LinkNodeComponent,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
  ],
})
export class NetworkNodeListItemComponent {
  readonly routeType = input.required<RouteType>();
  readonly routeScope = input.required<RouteScope>();
  readonly rowNumber = input.required<number>();
  readonly row = input.required<NetworkNodeRow>();
}
