import {Component, Input} from "@angular/core";
import {NetworkTypes} from "../../../../kpn/common/network-types";
import {NodeInfo} from "../../../../kpn/shared/node-info";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {List} from "immutable";
import {Util} from "../../../../components/shared/util";
import {I18nService} from "../../../../i18n/i18n.service";

@Component({
  selector: "node-summary",
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
        <kpn-network-type [networkType]="networkType">&nbsp;<span i18n="@@node.node">network node</span></kpn-network-type>
      </p>

      <p *ngIf="nodeInfo.country">
        {{countryName()}}
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

  constructor(private i18nService: I18nService) {
  }

  networkTypes(): List<NetworkType> {
    return NetworkTypes.all.filter(networkType => this.nodeInfo.tags.has(networkType.id + "_ref"));
  }

  countryName(): string {
    return this.i18nService.name("country." + Util.safeGet(() => this.nodeInfo.country.domain));
  }
}
