import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-horse-node-tag",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.lost-horse-riding-node-tag">
      This node is no longer a valid horse riding network node because the rhn_ref tag has been removed.
    </p>
  `
})
export class FactLostHorseNodeTagComponent {
}
