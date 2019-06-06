import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Reference} from "../../../../kpn/shared/common/reference";
import {NodeOrphanRouteReference} from "../../../../kpn/shared/node/node-orphan-route-reference";

@Component({
  selector: "kpn-node-orphan-route-references",
  template: `
    <p *ngIf="references.isEmpty()" i18n="@@node.orphan-route-references.none">None</p>
    <p *ngFor="let reference of references">
      <kpn-icon-route-link [reference]="toReference(reference)"></kpn-icon-route-link>
    </p>
  `
})
export class NodeOrphanRouteReferencesComponent {
  @Input() references: List<NodeOrphanRouteReference>;

  toReference(ref: NodeOrphanRouteReference): Reference {
    return new Reference(ref.routeId, ref.routeName, ref.networkType, false);
  }

}
