import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Check} from "../../../kpn/shared/check";

@Component({
  selector: "kpn-network-fact-checks",
  template: `

    <table title="node integrity check failures" i18n-title="@@TODO" class="kpn-table">
      <thead>
      <tr>
        <th>Node</th> <!--@@ Knooppunt -->
        <th>Expected</th> <!--@@ Verwacht -->
        <th>Actual</th> <!--@@ Gevonden -->
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
