import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-unexpected-node',
  template: `
    <ng-container i18n="@@fact.description.route-unexpected-node">
      The route relation contains 1 or more unexpected nodes
    </ng-container>
  `
})
export class FactRouteUnexpectedNodeComponent {
}
