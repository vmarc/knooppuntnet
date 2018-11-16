import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-one-way',
  template: `
    <!--De route is getagged als bruikbaar in slechts 1 richting. Dit is OK.-->
    The route is tagged as useable in one direction only. This is OK.
  `
})
export class FactRouteOneWayComponent {
}
