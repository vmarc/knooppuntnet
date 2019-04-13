import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-integrity-check",
  template: `
    <ng-container i18n="@@fact.description.integrity-check">
      This network node has a tag that indicates the expected number of routes that arrive/depart in this node.
    </ng-container>
  `
})
export class FactIntegrityCheckComponent {
}
