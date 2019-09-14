import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-network-type-not-tagged",
  template: `
    <p i18n="@@fact.description.network-type-not-tagged">
      This network does not have a 'network:type=node_network' tag.
    </p>
  `
})
export class FactNetworkTypeNotTaggedComponent {
}
