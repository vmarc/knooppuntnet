import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Check } from '@api/common';
import { LinkNodeComponent } from '@app/components/shared/link';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';

@Component({
  selector: 'kpn-network-fact-checks',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="network-fact-checks">
      <table
        title="node integrity check failures"
        i18n-title="@@network-facts.checks-table.title"
        class="kpn-table"
      >
        <thead>
          <tr>
            <th class="nr"></th>
            <th i18n="@@network-facts.checks-table.node">Node</th>
            <th i18n="@@network-facts.checks-table.expected">Expected</th>
            <th i18n="@@network-facts.checks-table.actual">Actual</th>
          </tr>
        </thead>
        <tbody>
          @for (check of checks(); track $index) {
            <tr>
              <td>
                <span class="kpn-thin">{{ $index + 1 }}</span>
              </td>
              <td class="no-indent">
                <div class="kpn-align-center">
                  <kpn-action-button-node [nodeId]="check.nodeId" />
                  <kpn-link-node [nodeId]="check.nodeId" [nodeName]="check.nodeName" />
                </div>
              </td>
              <td>
                {{ check.expected }}
              </td>
              <td>
                {{ check.actual }}
              </td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  `,
  styles: `
    .network-fact-checks table td {
      padding-left: 1em;
      padding-right: 1em;
      vertical-align: middle;
    }
    .no-indent {
      padding-left: 0 !important;
    }
  `,
  standalone: true,
  imports: [LinkNodeComponent, ActionButtonNodeComponent],
})
export class NetworkFactChecksComponent {
  checks = input.required<Check[]>();
}
