import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-hiking-node-tag",
  template: `
    <ng-container i18n="@@fact.description.lost-hiking-node-tag">
      This node is no longer a valid hikingnetwork node because the rwn_ref tag has been removed.
    </ng-container>
  `
})
export class FactLostHikingNodeTagComponent {
}
