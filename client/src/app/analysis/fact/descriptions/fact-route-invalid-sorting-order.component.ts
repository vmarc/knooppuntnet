import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-invalid-sorting-order',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-invalid-sorting-order">
      The route is valid, but the sorting order of the ways is incorrect.
    </p>
  `,
})
export class FactRouteInvalidSortingOrderComponent {}
