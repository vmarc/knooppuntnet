import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-incomplete-ok',
  template: `
    <markdown>
      <ng-container i18n="@@fact.description.route-incomplete-ok">
        The route is marked as having an incomplete definition. A route definition is explicitely
        marked incomplete by adding a tag _"fixme"_ with value _"incomplete"_ in the route relation. But
        after analysis, the route seems to be ok.
      </ng-container>
    </markdown>
  `
})
export class FactRouteIncompleteOkComponent {
}
