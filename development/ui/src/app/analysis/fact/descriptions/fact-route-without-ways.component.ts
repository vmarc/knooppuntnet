import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-without-ways',
  template: `
    <!--De route bevat geen enkele weg (we verwachten ten minste 1 weg).-->
    The route does not contain any ways (we expect the route to contain at least 1 way).
  `
})
export class FactRouteWithoutWaysComponent {
}
