import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network/network-attributes';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkNetworkDetailsComponent } from '@app/shared/components/link/link-network-details.component';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { SubsetNetworkHappyComponent } from './subset-network-happy.component';

@Component({
  selector: 'ui-subset-network-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-table
      #networksTable
      nzBordered
      [nzFrontPagination]="false"
      [nzData]="networks()"
      nzSize="small"
    >
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
          <th i18n="@@subset-networks.table.ok">OK</th>
        </tr>
      </thead>

      <tbody>
        @for (network of networksTable.data; track network.id) {
          <tr>
            <td>
              <ui-link-network-details
                [networkId]="network.id"
                [routeType]="network.routeType"
                [networkName]="network.name"
              />
            </td>
            <td class="happy">
              <ui-subset-network-happy [network]="network" />
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
            <td [ngClass]="{ 'kpn-warning': network.brokenRouteCount > 0 }" class="number-value">
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
        }
      </tbody>
    </nz-table>
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
  imports: [
    IntegerFormatPipe,
    LinkNetworkDetailsComponent,
    NgClass,
    NzTableComponent,
    SubsetNetworkHappyComponent,
  ],
})
export class SubsetNetworkTableComponent {
  readonly networks = input.required<NetworkAttributes[]>();
}
