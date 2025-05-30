import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteNode } from '@api/common/route/route-node';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'ui-route-redundant-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (node of nodes(); track node) {
      <p>
        <ui-route-node [node]="node" title="marker-icon-yellow-small.png" />
      </p>
    }
  `,
  imports: [RouteNodeComponent],
})
export class RouteRedundantNodesComponent {
  nodes = input.required<RouteNode[]>();
}
