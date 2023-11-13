import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteInfoAnalysis } from '@api/common/route';
import { RouteNodeComponent } from './route-node.component';

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
  standalone: true,
  imports: [NgIf, NgFor, RouteNodeComponent],
})
export class RouteFreeNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
