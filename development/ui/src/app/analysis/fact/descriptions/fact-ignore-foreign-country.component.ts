import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-ignore-foreign-country",
  template: `
    <ng-container i18n="@@fact.description.ignore-foreign-country">
      Not included in analysis: located in a country different from The Netherlands, Belgium or Germany.
    </ng-container>
  `
})
export class FactIgnoreForeignCountryComponent {
}
