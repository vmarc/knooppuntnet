import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';
import { RouteNodeComponent } from './route-node.component';

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
  standalone: true,
  imports: [NgIf, NgFor, RouteNodeComponent],
})
export class RouteStartNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
