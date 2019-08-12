import {Component, Input} from "@angular/core";
import {RouteInfoAnalysis} from "../../../kpn/shared/route/route-info-analysis";

@Component({
  selector: "kpn-route-end-nodes",
  template: `
    <p *ngIf="analysis.endNodes.isEmpty()">?</p>
    <p *ngFor="let node of analysis.endNodes">
      <kpn-route-node [node]="node" title="marker-icon-red-small.png"></kpn-route-node>
    </p>
    <p *ngFor="let node of analysis.endTentacleNodes">
      <kpn-route-node [node]="node" title="marker-icon-purple-small.png"></kpn-route-node>
    </p>
  `
})
export class RouteEndNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
