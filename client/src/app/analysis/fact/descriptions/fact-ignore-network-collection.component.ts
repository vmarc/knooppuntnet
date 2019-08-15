import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-ignore-network-collection",
  template: `
    <p i18n="@@fact.description.ignore-network-collection">
      Not included in the analysis because this relation is a collection of networks.
    </p>
  `
})
export class FactIgnoreNetworkCollectionComponent {
}
