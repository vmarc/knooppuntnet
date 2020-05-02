import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-was-orphan",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.was-orphan">
      No longer an orphan.
    </p>
  `
})
export class FactWasOrphanComponent {
}
