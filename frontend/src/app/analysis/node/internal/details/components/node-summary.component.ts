import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { CountryNameComponent } from '@app/shared/components/country-name.component';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { RouteScopeNameComponent } from '@app/shared/components/route-scope-name.component';
import { RouteTypeComponent } from '@app/shared/components/route-type.component';
import { MarkdownComponent } from 'ngx-markdown';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';

@Component({
  selector: 'ui-node-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    <div>
      @if (!nodeInfo().active) {
        <p class="kpn-warning" i18n="@@node.inactive">This network node is not active anymore.</p>
      }

      @if (nodeInfo().names.length > 1) {
        <table>
          @for (nodeName of nodeInfo().names; track nodeName) {
            <tr>
              <td class="network-name">
                {{ nodeName.name }}
              </td>
              <td>
                <div class="kpn-line">
                  <ui-route-type [routeType]="nodeName.routeType">
                    <span i18n="@@node.node" class="route-type">network node</span>
                    <span class="kpn-brackets">
                      <ui-route-scope-name [routeScope]="nodeName.routeScope" />
                    </span>
                  </ui-route-type>
                </div>
              </td>
            </tr>
          }
        </table>
      }

      @if (nodeInfo().names.length === 1) {
        <div>
          @for (nodeName of nodeInfo().names; track nodeName) {
            <p>
              <ui-route-type [routeType]="nodeName.routeType">
                <span i18n="@@node.node" class="route-type">network node</span>
              </ui-route-type>
            </p>
          }
        </div>
      }

      @if (nodeInfo().country) {
        <p>
          <ui-country-name [country]="nodeInfo().country" />
        </p>
      }

      @if (nodeInfo().active && nodeInfo().orphan) {
        <p i18n="@@node.orphan">
          This network node does not belong to a known node network (orphan).
        </p>
      }

      @if (isProposed()) {
        <p class="kpn-line">
          <ui-icon-warning />
          <markdown i18n="@@node.proposed">
            Proposed: the network node is assumed to still be in a planning phase and likely not
            signposted in the field.
          </markdown>
        </p>
      }

      <div class="kpn-align-center">
        <span>{{ nodeInfo().id }}</span>
        <ui-action-button-node [nodeId]="nodeInfo().id" />
      </div>
    </div>
  `,
  styles: `
    .network-name {
      padding-right: 1em;
    }

    .route-type {
      padding-left: 0.4em;
    }
  `,
  imports: [
    ActionButtonNodeComponent,
    CountryNameComponent,
    IconWarningComponent,
    MarkdownComponent,
    RouteScopeNameComponent,
    RouteTypeComponent,
  ],
})
export class NodeSummaryComponent {
  readonly nodeInfo = input.required<NodeInfo>();

  isProposed(): boolean {
    return this.nodeInfo().names.some((name) => name.proposed);
  }
}
