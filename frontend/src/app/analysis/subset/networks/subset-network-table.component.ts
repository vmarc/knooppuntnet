import { NgClass, NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { LinkNetworkDetailsComponent } from '@app/components/shared/link';
import { SubsetNetworkHappyComponent } from './subset-network-happy.component';

@Component({
  selector: 'kpn-subset-network-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <thead>
        <tr>
          <th colSpan="2" rowSpan="2" i18n="@@subset-networks.table.network">
            Network
          </th>
          <th rowSpan="2" i18n="@@subset-networks.table.length">Length</th>
          <th rowSpan="2" i18n="@@subset-networks.table.nodes">Nodes</th>
          <th colSpan="3" i18n="@@subset-networks.table.routes">Routes</th>
          <th colSpan="3" i18n="@@subset-networks.table.integrity">
            Integrity
          </th>
          <th rowSpan="2" i18n="@@subset-networks.table.connections">
            Connections
          </th>
        </tr>
        <tr>
          <th></th>
          <th colSpan="2" i18n="@@subset-networks.table.broken">Broken</th>
          <th colSpan="2" i18n="@@subset-networks.table.integrity-nodes">
            Nodes
          </th>
          <th i18n="@@subset-networks.table.ok">OK</th>
        </tr>
      </thead>

      <tbody>
        <tr *ngFor="let network of networks">
          <td>
            <kpn-link-network-details
              [networkId]="network.id"
              [networkType]="network.networkType"
              [networkName]="network.name"
            />
          </td>
          <td class="happy">
            <kpn-subset-network-happy [network]="network" />
          </td>
          <td class="number-value kpn-km">
            {{ network.km | integer }}
          </td>
          <td class="number-value">
            {{ network.nodeCount | integer }}
          </td>
          <td class="number-value">
            {{ network.routeCount | integer }}
          </td>
          <td class="number-value">
            {{ network.brokenRouteCount }}
          </td>
          <td
            [ngClass]="{ 'kpn-warning': network.brokenRouteCount > 0 }"
            class="number-value"
          >
            {{ network.brokenRoutePercentage }}
          </td>
          <td class="number-value">
            {{ network.integrity.count }}
          </td>
          <td class="number-value">
            {{ network.integrity.coverage }}
          </td>
          <td class="number-value">
            {{ network.integrity.okRate }}
          </td>
          <td class="number-value">
            {{ network.connectionCount }}
          </td>
        </tr>
      </tbody>
    </table>
  `,
  styles: `
    .happy {
      min-width: 55px;
    }

    .number-value {
      white-space: nowrap;
      text-align: right;
    }
  `,
  standalone: true,
  imports: [
    IntegerFormatPipe,
    LinkNetworkDetailsComponent,
    NgClass,
    NgFor,
    SubsetNetworkHappyComponent,
  ],
})
export class SubsetNetworkTableComponent {
  @Input() networks: NetworkAttributes[];
}
