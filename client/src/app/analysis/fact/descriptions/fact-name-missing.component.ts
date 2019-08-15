import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-name-missing",
  template: `
    <p i18n="@@fact.description.name-missing">
      The network relation does not contain the mandatory tag with key "name".
    </p>
  `
})
export class FactNameMissingComponent {
}
