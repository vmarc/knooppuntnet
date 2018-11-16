import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-name-missing',
  template: `
    <!--De route relatie bevat geen _"note"_ tag met de route naam.-->
    <markdown>
      The route relation does not have a _"note"_ tag with the route name.
    </markdown>
  `
})
export class FactRouteNameMissingComponent {
}
