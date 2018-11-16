import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-network-extra-member-node',
  template: `
    <!--De netwerk relatie bevat 1 of meerdere onverwachte knopen (we verwachten enkel knopen die
    ook echt een knooppunt definitie zijn, of een netwerk kaart).-->
    <markdown>
      The network relation contains members of type _"node"_ that are unexpected (we expect
      only network nodes or information maps as members in the network relation).
    </markdown>

  `
})
export class FactNetworkExtraMemberNodeComponent {
}
