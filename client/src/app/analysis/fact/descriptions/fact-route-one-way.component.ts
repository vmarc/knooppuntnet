import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-one-way",
  template: `
    <ng-container i18n="@@fact.description.route-one-way">
      The route is tagged as useable in one direction only. This is OK.
    </ng-container>
  `
})
export class FactRouteOneWayComponent {
}
