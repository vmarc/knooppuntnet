import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NodeInfo} from "../../../kpn/api/common/node-info";
import {NetworkType} from "../../../kpn/api/custom/network-type";

@Component({
  selector: "kpn-node-summary",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>

      <p>
        <kpn-osm-link-node [nodeId]="nodeInfo.id"></kpn-osm-link-node>
        <span class="kpn-brackets-link">
          <kpn-josm-node [nodeId]="nodeInfo.id"></kpn-josm-node>
        </span>
      </p>

      <p *ngIf="!nodeInfo.active" class="warning" i18n="@@node.inactive">
        This network node is not active anymore.
      </p>

      <table *ngIf="hasMultipleNames()">
        <tr *ngFor="let networkType of networkTypes()">
          <td class="network-name">
            {{networkName(networkType)}}
          </td>
          <td>
            <kpn-network-type [networkType]="networkType">
              <span i18n="@@node.node" class="network-type">network node</span>
            </kpn-network-type>
          </td>
        </tr>
      </table>

      <div *ngIf="!hasMultipleNames()">
        <p *ngFor="let networkType of networkTypes()">
          <kpn-network-type [networkType]="networkType">
            <span i18n="@@node.node" class="network-type">network node</span>
          </kpn-network-type>
        </p>
      </div>

      <p *ngIf="nodeInfo.country">
        <kpn-country-name [country]="nodeInfo.country"></kpn-country-name>
      </p>

      <p *ngIf="nodeInfo.active && nodeInfo.orphan" i18n="@@node.orphan">
        This network node does not belong to a known node network (orphan).
      </p>

    </div>
  `,
  styles: [`
    .network-name {
      padding-right: 1em;
    }

    .network-type {
      padding-left: 0.4em;
    }
  `]
})
export class NodeSummaryComponent {

  @Input() nodeInfo: NodeInfo;

  hasMultipleNames(): boolean {
    return this.nodeInfo.names.size > 1;
  }

  networkTypes(): List<NetworkType> {
    return NetworkType.all.filter(networkType => this.networkName(networkType));
  }

  networkName(networkType: NetworkType): string {
    const nodeName = this.nodeInfo.names.find(name => name.scopedNetworkType.networkType.id === networkType.id);
    if (nodeName) {
      return nodeName.name;
    }
    return undefined;
  }

}
