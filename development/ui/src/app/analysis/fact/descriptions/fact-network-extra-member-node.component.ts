import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-network-extra-member-node',
  template: `
    <markdown i18n="@@fact.description.network-extra-member-node">
      The network relation contains members of type _"node"_ that are unexpected (we expect
      only network nodes or information maps as members in the network relation).
    </markdown>
  `
})
export class FactNetworkExtraMemberNodeComponent {
}
