import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-bicycle-node-tag",
  template: `
    <p i18n="@@fact.description.lost-bicycle-node-tag">
      This node is no longer a valid bicylenetwork node because the rcn_ref tag has been removed.
    </p>
  `
})
export class FactLostBicycleNodeTagComponent {
}
