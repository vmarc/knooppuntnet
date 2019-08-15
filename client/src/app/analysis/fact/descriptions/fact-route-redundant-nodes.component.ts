import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-redundant-nodes",
  template: `
    <p i18n="@@fact.description.route-redundant-nodes">
      The ways of this route contain extra nodes other than the start and end nodes.
    </p>
  `
})
export class FactRouteRedundantNodesComponent {
}
