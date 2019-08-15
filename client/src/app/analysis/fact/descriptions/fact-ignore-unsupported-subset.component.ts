import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-ignore-unsupported-subset",
  template: `
    <p i18n="@@fact.description.ignore-unsupported-subset">
      Not included in analysis: no hiking node networks in Germany.
    </p>
  `
})
export class FactIgnoreUnsupportedSubsetComponent {
}
