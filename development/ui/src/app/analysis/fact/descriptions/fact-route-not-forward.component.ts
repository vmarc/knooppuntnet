import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-not-forward',
  template: `
    <!-- Er is geen verbinding van het startknooppunt naar het eindknooppunt (heen weg).-->
    There is no path in the forward direction (from start node to end node).
  `
})
export class FactRouteNotForwardComponent {
}
