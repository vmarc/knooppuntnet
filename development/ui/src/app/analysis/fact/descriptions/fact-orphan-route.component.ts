import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-orphan-route',
  template: `
    <!--Deze route maakt geen deel uit van een netwerk. Deze route wordt niet
    |teruggevonden in een geldige netwerk relatie.-->
    This route does not belong to a network. The route was not added as a member to
    a valid network relation.
  `
})
export class FactOrphanRouteComponent {
}
