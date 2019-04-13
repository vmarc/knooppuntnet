import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-overlapping-ways",
  template: `
    <ng-container i18n="@@fact.description.route-overlapping-ways">
      No detailed route analysis is performed because the route contains overlapping ways.
    </ng-container>
  `
})
export class FactRouteOverlappingWaysComponent {
}
