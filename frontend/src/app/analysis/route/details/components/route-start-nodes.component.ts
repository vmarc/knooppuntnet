import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteNodes } from '@api/common/route/route-nodes';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'kpn-route-start-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (hasNodes()) {
      @if (hasStartNode()) {
        <p>
          <kpn-route-node [node]="nodes().startNode" title="marker-icon-green-small.png" />
        </p>
      }
      @for (node of nodes().startTentacleNodes; track node) {
        <p>
          <kpn-route-node [node]="node" title="marker-icon-orange-small.png" />
        </p>
      }
    } @else {
      <!-- eslint-disable @angular-eslint/template/i18n -->
      <p>?</p>
    }
  `,
  imports: [RouteNodeComponent],
})
export class RouteStartNodesComponent {
  nodes = input.required<RouteNodes>();
  hasStartNode = computed(() => this.nodes().startNode);
  hasTentacles = computed(() => this.nodes().startTentacleNodes?.length > 0);
  hasNodes = computed(() => this.hasStartNode() || this.hasTentacles());
}
