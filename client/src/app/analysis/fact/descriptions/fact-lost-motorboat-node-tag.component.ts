import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-motorboat-node-tag",
  template: `
    <ng-container i18n="@@fact.description.lost-motorboat-node-tag">
      This node is no longer a valid motorboatnetwork node because the rmn_ref tag has been removed.
    </ng-container>
  `
})
export class FactLostMotorboatNodeTagComponent {
}
