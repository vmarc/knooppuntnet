import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../kpn/shared/common/ref";

@Component({
  selector: "kpn-network-fact-nodes",
  template: `
    <div *ngIf="nodes.size == 1">
      <span i18n="TODO">Node</span>:
    </div>
    <div *ngIf="nodes.size > 1">
      <span i18n="TODO">Nodes</span>:
    </div>
    <div class="kpn-comma-list">
      <kpn-link-node
        *ngFor="let node of nodes"
        [nodeId]="node.id" 
        [nodeName]="node.name">
      </kpn-link-node>
    </div>
  `
})
export class NetworkFactNodesComponent {
  @Input() nodes: List<Ref>;
}
