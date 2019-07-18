import {Component, Input} from "@angular/core";
import {RouteInfoAnalysis} from "../../../../kpn/shared/route/route-info-analysis";

@Component({
  selector: "kpn-route-start-nodes",
  template: `
    <p *ngIf="analysis.startNodes.isEmpty()">?</p>
    <p *ngFor="let node of analysis.startNodes">
      <kpn-route-node [node]="node" title="marker-icon-green-small.png"></kpn-route-node>
    </p>
    <p *ngFor="let node of analysis.startTentacleNodes">
      <kpn-route-node [node]="node" title="marker-icon-orange-small.png"></kpn-route-node>
    </p>
  `
})
export class RouteStartNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
