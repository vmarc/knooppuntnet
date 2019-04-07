import {Component, Input} from "@angular/core";
import {NetworkTypes} from "../../../../kpn/common/network-types";
import {NodeInfo} from "../../../../kpn/shared/node-info";

@Component({
  selector: 'node-summary',
  template: `
    <div>

      <p>
        <osm-link-node nodeId="{{nodeInfo.id}}"></osm-link-node>
        (
        <josm-node nodeId="{{nodeInfo.id}}"></josm-node>
        )
      </p>

      <p *ngIf="!nodeInfo.active" class="warning" i18n="@@node.inactive">
        This network node is not active anymore.
      </p>

      <p *ngFor="let networkType of networkTypes()">
        <kpn-network-type [networkType]="networkType"></kpn-network-type>
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

  networkTypes() {
    return NetworkTypes.all.filter(networkType => this.nodeInfo.tags.has(networkType.name + "_ref"));
  }

}
