import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-not-one-way",
  template: `
    <p i18n="@@fact.description.route-not-one-way">
      The route is tagged as useable in one direction only, but the analysis logic does find ways in both directions.
    </p>
  `
})
export class FactRouteNotOneWayComponent {
}
