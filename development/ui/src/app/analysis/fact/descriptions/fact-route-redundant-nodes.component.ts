import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-redundant-nodes',
  template: `
    <ng-container i18n="@@fact.description.route-redundant-nodes">
      The ways of this route contain extra nodes other than the start and end nodes.
    </ng-container>
  `
})
export class FactRouteRedundantNodesComponent {
}
