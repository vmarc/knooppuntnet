import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-orphan-node',
  template: `
    <!--Op zichzelf staande knooppunt (niet teruggevonden in een netwerk).
    |Dit knooppunt werd niet teruggevonden als deel van een geldige netwerk relatie,
    |of als deel van een geldige route relatie (dit kan een route relatie zijn die
    |deel uitmaakt van een network relatie, maar ook een op zichzelf staande route
    |relatie die geen deel uitmaakt van een network relatie).
    -->
    This node does not belong to a network.
    The node was not added as a member to a valid network relation, and also not added
    as a member to valid route relation (that itself was added as a member to a valid
    network relation or is an orphan route).
  `
})
export class FactOrphanNodeComponent {
}
