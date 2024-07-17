import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteNodes } from '@api/common/route/route-nodes';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'kpn-route-end-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (hasNodes()) {
      @if (hasEndNode()) {
        <p>
          <kpn-route-node [node]="nodes().endNode" title="marker-icon-red-small.png" />
        </p>
      }
      @for (node of nodes().endTentacleNodes; track node) {
        <p>
          <kpn-route-node [node]="node" title="marker-icon-purple-small.png" />
        </p>
      }
    } @else {
      <!-- eslint-disable @angular-eslint/template/i18n -->
      <p>?</p>
    }
  `,
  standalone: true,
  imports: [RouteNodeComponent],
})
export class RouteEndNodesComponent {
  nodes = input.required<RouteNodes>();
  hasEndNode = computed(() => this.nodes().endNode);
  hasTentacles = computed(() => this.nodes().endTentacleNodes?.length > 0);
  hasNodes = computed(() => this.hasEndNode() || this.hasTentacles());
}
