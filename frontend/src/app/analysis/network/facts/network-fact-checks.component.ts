import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Check } from '@api/common';
import { LinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-checks',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
            <td>
              <kpn-link-node [nodeId]="check.nodeId" [nodeName]="check.nodeName" />
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
  `,
  styles: `
    .nr {
      min-width: 2em;
    }
  `,
  standalone: true,
  imports: [LinkNodeComponent],
})
export class NetworkFactChecksComponent {
  checks = input.required<Check[]>();
}
