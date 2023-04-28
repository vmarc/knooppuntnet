import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'kpn-route-redundant-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngFor="let node of analysis.map.redundantNodes">
      <kpn-route-node [node]="node" title="marker-icon-yellow-small.png" />
    </p>
  `,
  standalone: true,
  imports: [NgFor, RouteNodeComponent],
})
export class RouteRedundantNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
