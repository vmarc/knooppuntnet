import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-inline-skate-node-tag",
  template: `
    <ng-container i18n="@@fact.description.lost-inline-skate-node-tag">
      This node is no longer a valid inline skating network node because the rin_ref tag has been removed.
    </ng-container>
  `
})
export class FactLostInlineSkateNodeTagComponent {
}
