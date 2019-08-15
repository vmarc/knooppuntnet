import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-hiking-node-tag",
  template: `
    <p i18n="@@fact.description.lost-hiking-node-tag">
      This node is no longer a valid hikingnetwork node because the rwn_ref tag has been removed.
    </p>
  `
})
export class FactLostHikingNodeTagComponent {
}
