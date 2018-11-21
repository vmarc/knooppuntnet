import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-node-name-mismatch',
  template: `
    <markdown>
      <ng-container i18n="@@fact.description.route-node-name-mismatch">
        The route name in the _note_ tag in the route relation does not match the expected name as
        derived from the start and end node of the route.
      </ng-container>
    </markdown>
  `
})
export class FactRouteNodeNameMismatchComponent {
}
