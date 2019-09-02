import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-horse-node-tag",
  template: `
    <p i18n="@@fact.description.lost-horse-riding-node-tag">
      This node is no longer a valid horse riding network node because the rhn_ref tag has been removed.
    </p>
  `
})
export class FactLostHorseNodeTagComponent {
}
