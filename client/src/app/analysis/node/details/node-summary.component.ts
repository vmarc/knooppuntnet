import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkType} from "../../../kpn/shared/network-type";
import {NodeInfo} from "../../../kpn/shared/node-info";

@Component({
  selector: "kpn-node-summary",
  template: `
    <div>

      <p>
        <kpn-osm-link-node nodeId="{{nodeInfo.id}}"></kpn-osm-link-node>
        (
        <kpn-josm-node nodeId="{{nodeInfo.id}}"></kpn-josm-node>
        )
      </p>

      <p *ngIf="!nodeInfo.active" class="warning" i18n="@@node.inactive">
        This network node is not active anymore.
      </p>

      <p *ngFor="let networkType of networkTypes()">
        <kpn-network-type [networkType]="networkType">&nbsp;<span i18n="@@node.node">network node</span></kpn-network-type>
      </p>

      <p *ngIf="nodeInfo.country">
        <kpn-country-name [country]="nodeInfo.country"></kpn-country-name>
      </p>

      <p *ngIf="nodeInfo.active && nodeInfo.orphan" i18n="@@node.orphan">
        This network node does not belong to a known node network (orphan).
      </p>

      <p *ngIf="nodeInfo.ignored" i18n="@@node.ignored">
        This network node is not included in the analysis.
      </p>

    </div>
  `
})
export class NodeSummaryComponent {

  @Input() nodeInfo: NodeInfo;

  networkTypes(): List<NetworkType> {
    return NetworkType.all.filter(networkType => this.nodeInfo.tags.has(networkType.id + "_ref"));
  }

}
