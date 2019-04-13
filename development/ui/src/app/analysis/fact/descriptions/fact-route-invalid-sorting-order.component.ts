import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-invalid-sorting-order",
  template: `
    <ng-container i18n="@@fact.description.route-invalid-sorting-order">
      The route is valid, but the sorting order of the ways is incorrect.
    </ng-container>
  `
})
export class FactRouteInvalidSortingOrderComponent {
}
