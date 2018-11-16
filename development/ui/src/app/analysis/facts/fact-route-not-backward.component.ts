import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-not-backward',
  template: `
    <!--Er is geen verbinding van het eindknooppunt naar het startknooppunt (terug weg).-->
    There is no path in the backward direction (from end node to start node).
  `
})
export class FactRouteNotBackwardComponent {
}
