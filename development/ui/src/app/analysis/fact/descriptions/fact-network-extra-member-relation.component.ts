import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-network-extra-member-relation',
  template: `
    <!--De netwerk relatie bevat overwachte relaties. In network relaties verwachten we enkel
    route relaties of knooppunten, geen relaties die geen route relaties zijn.
    -->
    <markdown>
      The network relation contains members of type _"relation"_ that are unexpected
      (expect only valid route relations or network nodes as members in the node network relation).
    </markdown>
  `
})
export class FactNetworkExtraMemberRelationComponent {
}
