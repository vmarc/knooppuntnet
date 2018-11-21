import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-name-missing',
  template: `
    <markdown>
      <ng-container i18n="@@fact.description.route-name-missing">
        The route relation does not have a _"note"_ tag with the route name.
      </ng-container>
    </markdown>
  `
})
export class FactRouteNameMissingComponent {
}
