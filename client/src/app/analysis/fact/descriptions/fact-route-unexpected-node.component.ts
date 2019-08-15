import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-unexpected-node",
  template: `
    <p i18n="@@fact.description.route-unexpected-node">
      The route relation contains 1 or more unexpected nodes
    </p>
  `
})
export class FactRouteUnexpectedNodeComponent {
}
