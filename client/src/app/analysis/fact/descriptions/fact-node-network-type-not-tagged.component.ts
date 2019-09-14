import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-node-network-type-not-tagged",
  template: `
    <p i18n="@@fact.description.node-network-type-not-tagged">
      This node does not have a 'network:type=node_network' tag.
    </p>
  `
})
export class FactNodeNetworkTypeNotTaggedComponent {
}
