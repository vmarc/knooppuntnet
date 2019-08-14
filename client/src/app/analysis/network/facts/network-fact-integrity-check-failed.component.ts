import {Component, Input} from "@angular/core";
import {NetworkIntegrityCheckFailed} from "../../../kpn/shared/network-integrity-check-failed";

@Component({
  selector: "kpn-network-fact-integrity-check-failed",
  template: `

    <kpn-fact-header
      [factName]="'IntegrityCheckFailed'"
      [factCount]="integrityCheckFailed.checks.size">
    </kpn-fact-header>

    <table title="node integrity check failures" i18n-title="@@TODO" class="kpn-table">
      <thead>
      <tr>
        <th>Node</th> <!--@@ Knooppunt -->
        <th>Expected</th> <!--@@ Verwacht -->
        <th>Actual</th> <!--@@ Gevonden -->
      </tr>
      </thead>
      <tbody>
      <tr *ngFor="let check of integrityCheckFailed.checks">
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
export class NetworkFactIntegrityCheckFailedComponent {
  @Input() integrityCheckFailed: NetworkIntegrityCheckFailed;
}
