import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route/route-info-analysis';

@Component({
  selector: 'kpn-route-free-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <p *ngIf="analysis.map.freeNodes.length === 0">?</p>
    <p *ngFor="let node of analysis.map.freeNodes">
      <kpn-route-node [node]="node" title="marker-icon-blue-small.png" />
    </p>
  `,
})
export class RouteFreeNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
