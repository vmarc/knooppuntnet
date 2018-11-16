import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-node-name-mismatch',
  template: `
    <!--De route naam in de _"note"_ tag in de route relatie is anders dan wat we verwachten op basis van """ +
    "de start en eind knooppunten van de route."
    -->
    MARKED The route name in the _note_ tag in the route relation does not match the expected name as
    derived from the start and end node of the route.
  `
})
export class FactRouteNodeNameMismatchComponent {
}
