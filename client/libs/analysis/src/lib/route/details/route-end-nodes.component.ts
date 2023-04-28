import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';
import { RouteNodeComponent } from './route-node.component';

@Component({
  selector: 'kpn-route-end-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <p *ngIf="analysis.map.endNodes.length === 0">?</p>
    <!-- eslint-enable @angular-eslint/template/i18n -->
    <p *ngFor="let node of analysis.map.endNodes">
      <kpn-route-node [node]="node" title="marker-icon-red-small.png" />
    </p>
    <p *ngFor="let node of analysis.map.endTentacleNodes">
      <kpn-route-node [node]="node" title="marker-icon-purple-small.png" />
    </p>
  `,
  standalone: true,
  imports: [NgIf, NgFor, RouteNodeComponent],
})
export class RouteEndNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
