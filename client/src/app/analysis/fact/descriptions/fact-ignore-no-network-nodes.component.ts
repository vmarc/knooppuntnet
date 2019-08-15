import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-ignore-no-network-nodes",
  template: `
    <p i18n="@@fact.description.ignore-no-network-nodes">
      Not included in analysis: route does not contain network nodes.
    </p>
  `
})
export class FactIgnoreNoNetworkNodesComponent {
}
