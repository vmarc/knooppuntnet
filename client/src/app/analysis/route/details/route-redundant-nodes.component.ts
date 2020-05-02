import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {RouteInfoAnalysis} from "../../../kpn/api/common/route/route-info-analysis";

@Component({
  selector: "kpn-route-redundant-nodes",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngFor="let node of analysis.map.redundantNodes">
      <kpn-route-node [node]="node" title="marker-icon-yellow-small.png"></kpn-route-node>
    </p>
  `
})
export class RouteRedundantNodesComponent {
  @Input() analysis: RouteInfoAnalysis;
}
