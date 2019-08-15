import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-not-backward",
  template: `
    <p i18n="@@fact.description.route-not-backward">
      There is no path in the backward direction (from end node to start node).
    </p>
  `
})
export class FactRouteNotBackwardComponent {
}
