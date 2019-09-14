import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-network-type-not-tagged",
  template: `
    <p i18n="@@fact.description.route-network-type-not-tagged">
      This route does not have a 'network:type=node_network' tag.
    </p>
  `
})
export class FactRouteNetworkTypeNotTaggedComponent {
}
