import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {RouteInfoAnalysis} from '../../../kpn/api/common/route/route-info-analysis';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-route-start-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="analysis.map.startNodes.isEmpty()">?</p>
    <p *ngFor="let node of analysis.map.startNodes">
      <kpn-route-node [node]="node" title="marker-icon-green-small.png"></kpn-route-node>
    </p>
    <p *ngFor="let node of analysis.map.startTentacleNodes">
      <kpn-route-node [node]="node" title="marker-icon-orange-small.png"></kpn-route-node>
    </p>
  `
})
export class RouteStartNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
