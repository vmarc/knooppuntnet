import {Component, Input, OnInit} from "@angular/core";
import {NetworkIntegrityCheckFailed} from "../../../kpn/shared/network-integrity-check-failed";
import {FactLevel} from "../../fact/fact-level";
import {Facts} from "../../fact/facts";

@Component({
  selector: 'kpn-network-fact-integrity-check-failed',
  template: `

    <div class="kpn-line">
      <span class="kpn-thick">
        <kpn-fact-name factName="IntegrityCheckFailed"></kpn-fact-name>
      </span>
      <span>
        ({{integrityCheckFailed.checks.size}})
      </span>
      <kpn-fact-level [factLevel]="factLevel('NetworkExtraMemberNode')" class="level"></kpn-fact-level>
    </div>
    <div>
      <kpn-fact-description factName="NetworkExtraMemberNode"></kpn-fact-description>
    </div>
    <table title="node integrity check failures"  i18n-title="@@TODO" class="kpn-table">
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

  factLevel(factName: string): FactLevel {
    return Facts.factLevels.get(factName);
  }

}
