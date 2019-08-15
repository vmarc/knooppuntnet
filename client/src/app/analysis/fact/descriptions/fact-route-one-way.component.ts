import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-one-way",
  template: `
    <p i18n="@@fact.description.route-one-way">
      The route is tagged as useable in one direction only. This is OK.
    </p>
  `
})
export class FactRouteOneWayComponent {
}
