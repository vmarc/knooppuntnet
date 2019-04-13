import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-not-forward",
  template: `
    <ng-container i18n="@@fact.description.route-not-forward">
      There is no path in the forward direction (from start node to end node).
    </ng-container>
  `
})
export class FactRouteNotForwardComponent {
}
