import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-become-orphan",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.become-orphan">
      Has become orphan.
    </p>
  `
})
export class FactBecomeOrphanComponent {
}
