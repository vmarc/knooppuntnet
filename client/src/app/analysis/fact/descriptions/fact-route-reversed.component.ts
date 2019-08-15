import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-reversed",
  template: `
    <p i18n="@@fact.description.route-reversed">
      The ways in this route are in reverse order (from high node number to low node number).
    </p>
  `
})
export class FactRouteReversedComponent {
}
