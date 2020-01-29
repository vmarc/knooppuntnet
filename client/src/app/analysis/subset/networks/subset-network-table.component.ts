import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkAttributes} from "../../../kpn/api/common/network/network-attributes";

@Component({
  selector: "kpn-subset-network-table",
  template: `

    <table class="kpn-table">

      <thead>
      <tr>
        <th colSpan="2" rowSpan="2" i18n="@@subset-networks.table.network">Network</th>
        <th rowSpan="2" i18n="@@subset-networks.table.length">Length</th>
        <th rowSpan="2" i18n="@@subset-networks.table.nodes">Nodes</th>
        <th colSpan="3" i18n="@@subset-networks.table.routes">Routes</th>
        <th colSpan="3" i18n="@@subset-networks.table.integrity">Integrity</th>
        <th rowSpan="2" i18n="@@subset-networks.table.connections">Connections</th>
      </tr>
      <tr>
        <th></th>
        <th colSpan="2" i18n="@@subset-networks.table.broken">Broken</th>
        <th colSpan="2" i18n="@@subset-networks.table.integrity-nodes">Nodes</th>
        <th>OK</th>
      </tr>
      </thead>

      <tbody>
      <tr *ngFor="let network of networks">
        <td>
          <kpn-link-network-details [networkId]="network.id" [title]="network.name"></kpn-link-network-details>
        </td>
        <td class="happy">
          <kpn-subset-network-happy [network]="network"></kpn-subset-network-happy>
        </td>
        <td class="kpn-km">
          {{network.km}}
        </td>
        <td>
          {{network.nodeCount}}
        </td>
        <td>
          {{network.routeCount}}
        </td>
        <td>
          {{network.brokenRouteCount}}
        </td>
        <td>
          {{network.brokenRoutePercentage}}
        </td>
        <td>
          {{network.integrity.count}}
        </td>
        <td>
          {{network.integrity.coverage}}
        </td>
        <td>
          {{network.integrity.okRate}}
        </td>
        <td>
          {{network.connectionCount}}
        </td>
      </tr>
      </tbody>
    </table>
  `,
  styles: [`

    table {
      width: 100%;
    }

    .happy {
      min-width: 55px;
    }

  `]
})
export class SubsetNetworkTableComponent {
  @Input() networks: List<NetworkAttributes> = List();
}
