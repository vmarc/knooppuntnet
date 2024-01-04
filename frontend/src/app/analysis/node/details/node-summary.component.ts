import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NodeInfo } from '@api/common';
import { CountryNameComponent } from '@app/components/shared';
import { NetworkScopeNameComponent } from '@app/components/shared';
import { NetworkTypeComponent } from '@app/components/shared';
import { JosmNodeComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-node-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p>
        <kpn-osm-link-node [nodeId]="nodeInfo.id" />
        <span class="kpn-brackets-link">
          <kpn-josm-node [nodeId]="nodeInfo.id" />
        </span>
      </p>

      @if (!nodeInfo.active) {
        <p class="kpn-warning" i18n="@@node.inactive">This network node is not active anymore.</p>
      }

      @if (nodeInfo.names.length > 1) {
        <table>
          @for (nodeName of nodeInfo.names; track nodeName) {
            <tr>
              <td class="network-name">
                {{ nodeName.name }}
              </td>
              <td>
                <div class="kpn-line">
                  <kpn-network-type [networkType]="nodeName.networkType">
                    <span i18n="@@node.node" class="network-type">network node</span>
                    <span class="kpn-brackets">
                      <kpn-network-scope-name [networkScope]="nodeName.networkScope" />
                    </span>
                  </kpn-network-type>
                </div>
              </td>
            </tr>
          }
        </table>
      }

      @if (nodeInfo.names.length === 1) {
        <div>
          @for (nodeName of nodeInfo.names; track nodeName) {
            <p>
              <kpn-network-type [networkType]="nodeName.networkType">
                <span i18n="@@node.node" class="network-type">network node</span>
              </kpn-network-type>
            </p>
          }
        </div>
      }

      @if (nodeInfo.country) {
        <p>
          <kpn-country-name [country]="nodeInfo.country" />
        </p>
      }

      @if (nodeInfo.active && nodeInfo.orphan) {
        <p i18n="@@node.orphan">
          This network node does not belong to a known node network (orphan).
        </p>
      }

      @if (isProposed()) {
        <p class="kpn-line">
          <mat-icon svgIcon="warning" style="min-width: 24px" />
          <markdown i18n="@@node.proposed">
            Proposed: the network node is assumed to still be in a planning phase and likely not
            signposted in the field.
          </markdown>
        </p>
      }
    </div>
  `,
  styles: `
    .network-name {
      padding-right: 1em;
    }

    .network-type {
      padding-left: 0.4em;
    }
  `,
  standalone: true,
  imports: [
    CountryNameComponent,
    JosmNodeComponent,
    MarkdownModule,
    MatIconModule,
    NetworkScopeNameComponent,
    NetworkTypeComponent,
    OsmLinkNodeComponent,
  ],
})
export class NodeSummaryComponent {
  @Input() nodeInfo: NodeInfo;

  isProposed(): boolean {
    return this.nodeInfo.names.some((name) => name.proposed);
  }
}
