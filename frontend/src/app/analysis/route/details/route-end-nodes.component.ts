import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'kpn-route-end-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    @if (analysis().map.endNodes.length === 0) {
      <p>?</p>
    }
    <!-- eslint-enable @angular-eslint/template/i18n -->
    @for (node of analysis().map.endNodes; track node) {
      <p>
        <kpn-route-node [node]="node" title="marker-icon-red-small.png" />
      </p>
    }
    @for (node of analysis().map.endTentacleNodes; track node) {
      <p>
        <kpn-route-node [node]="node" title="marker-icon-purple-small.png" />
      </p>
    }
  `,
  standalone: true,
  imports: [RouteNodeComponent],
})
export class RouteEndNodesComponent {
  analysis = input<RouteInfoAnalysis | undefined>();
}
