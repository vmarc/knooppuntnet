import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-inline-skate-node-tag",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.lost-inline-skate-node-tag">
      This node is no longer a valid inline skating network node because the rin_ref tag has been removed.
    </p>
  `
})
export class FactLostInlineSkateNodeTagComponent {
}
