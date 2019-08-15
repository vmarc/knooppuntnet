import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-node-missing-in-ways",
  template: `
    <p i18n="@@fact.description.route-node-missing-in-ways">
      The end node and/or the begin node is not found in any of the ways of this route.
    </p>
  `
})
export class FactRouteNodeMissingInWaysComponent {
}
