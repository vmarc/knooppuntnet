import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-network-extra-member-way',
  template: `
    <!--
    |De netwerk relatie bevat overwachte wegen (in network relaties verwachten we enkel route
    |relaties of knooppunten, geen wegen).""".stripMargin
    -->
    <markdown>
      The network relation contains members of type _"way"_ (expect only route relations or
      network nodes as members in the node network relation).
    </markdown>
  `
})
export class FactNetworkExtraMemberWayComponent {
}
