import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-redundant-nodes',
  template: `
    <!-- Naast de begin en eind knooppunten bevinden er zich nog bijkomende knooppunten in de wegen van deze route.-->
    The ways of this route contain extra nodes other than the start and end nodes.
  `
})
export class FactRouteRedundantNodesComponent {
}
