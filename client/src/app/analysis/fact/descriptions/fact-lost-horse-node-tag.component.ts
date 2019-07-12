import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-horse-node-tag",
  template: `
    <ng-container i18n="@@fact.description.lost-hiking-node-tag">
      This node is no longer a valid horsenetwork node because the rhn_ref tag has been removed.
    </ng-container>
  `
})
export class FactLostHorseNodeTagComponent {
}
