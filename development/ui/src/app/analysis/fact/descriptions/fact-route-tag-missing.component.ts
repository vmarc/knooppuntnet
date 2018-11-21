import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-tag-missing',
  template: `
    <markdown>
      <ng-container i18n="@@fact.description.route-tag-missing">
        Routerelation does not contain the required _route_ tag.
      </ng-container>
    </markdown>
  `
})
export class FactRouteTagMissingComponent {
}
