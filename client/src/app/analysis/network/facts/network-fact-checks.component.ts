import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {List} from 'immutable';
import {Check} from '../../../kpn/api/common/check';

@Component({
  selector: 'kpn-network-fact-checks',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <table title="node integrity check failures" i18n-title="@@network-facts.checks-table.title" class="kpn-table">
      <thead>
      <tr>
        <th i18n="@@network-facts.checks-table.node">Node</th>
        <th i18n="@@network-facts.checks-table.expected">Expected</th>
        <th i18n="@@network-facts.checks-table.actual">Actual</th>
      </tr>
      </thead>
      <tbody>
      <tr *ngFor="let check of checks">
        <td>
          <kpn-link-node [nodeId]="check.nodeId" [nodeName]="check.nodeName"></kpn-link-node>
        </td>
        <td>
          {{check.expected}}
        </td>
        <td>
          {{check.actual}}
        </td>
      </tr>
      </tbody>
    </table>
  `
})
export class NetworkFactChecksComponent {
  @Input() checks: List<Check>;
}
