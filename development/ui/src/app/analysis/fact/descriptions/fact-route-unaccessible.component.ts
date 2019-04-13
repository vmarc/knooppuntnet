import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-unaccessible",
  template: `
    <ng-container i18n="@@fact.description.route-unaccessible">
      Part of the route does not seem
      context.gotoGlossaryEntry("accessible", "accessible"),
      .
    </ng-container>
  `
})
export class FactRouteUnaccessibleComponent {
}
