import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-overlapping-ways",
  template: `
    <p i18n="@@fact.description.route-overlapping-ways">
      No detailed route analysis is performed because the route contains overlapping ways.
    </p>
  `
})
export class FactRouteOverlappingWaysComponent {
}
