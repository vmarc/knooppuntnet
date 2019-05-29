import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-node-member-missing",
  template: `
    <ng-container i18n="@@fact.description.node-member-missing">
      The node is not member of the network relation.
    </ng-container>
  `
})
export class FactNodeMemberMissingComponent {
}
