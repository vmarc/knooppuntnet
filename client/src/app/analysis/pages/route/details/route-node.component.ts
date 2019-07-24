import {Component, Input} from "@angular/core";
import {RouteNetworkNodeInfo} from "../../../../kpn/shared/route/route-network-node-info";

@Component({
  selector: "kpn-route-node",
  template: `
    <p class="kpn-line">
      <img [src]="'/assets/images/' + title" class="image">
      <kpn-link-node [nodeId]="node.id" [nodeName]="node.alternateName"></kpn-link-node>
      (<osm-link-node [nodeId]="node.id"></osm-link-node>)
    </p>
  `
})
export class RouteNodeComponent {
  @Input() title: string;
  @Input() node: RouteNetworkNodeInfo;
}
