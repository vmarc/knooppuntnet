import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';

@Component({
  selector: 'kpn-route-start-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <p *ngIf="analysis.map.startNodes.length === 0">?</p>
    <p *ngFor="let node of analysis.map.startNodes">
      <kpn-route-node [node]="node" title="marker-icon-green-small.png" />
    </p>
    <p *ngFor="let node of analysis.map.startTentacleNodes">
      <kpn-route-node [node]="node" title="marker-icon-orange-small.png" />
    </p>
  `,
})
export class RouteStartNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
