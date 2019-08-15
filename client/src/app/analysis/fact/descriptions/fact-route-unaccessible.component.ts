import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-unaccessible",
  template: `
    <p i18n="@@fact.description.route-unaccessible">
      Part of the route does not seem
      context.gotoGlossaryEntry("accessible", "accessible"),
      .
    </p>
  `
})
export class FactRouteUnaccessibleComponent {
}
