import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-not-continious',
  template: `
    <!--De route is onderbroken: de begin- en eindknooppunten van deze route kunnen niet door wegen
    "verbonden worden in de heen en/of terug richting.-->
    The route is broken: the begin- and end-networknodes cannot be connected through ways either
    "in the forward direction or the backward direction or both.
  `
})
export class FactRouteNotContiniousComponent {
}
