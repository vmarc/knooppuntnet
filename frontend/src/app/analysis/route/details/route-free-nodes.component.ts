import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'kpn-route-free-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    @if (analysis().map.freeNodes.length === 0) {
      <p>?</p>
    }
    @for (node of analysis().map.freeNodes; track node) {
      <p>
        <kpn-route-node [node]="node" title="marker-icon-blue-small.png" />
      </p>
    }
  `,
  standalone: true,
  imports: [RouteNodeComponent],
})
export class RouteFreeNodesComponent {
  analysis = input<RouteInfoAnalysis | undefined>();
}
