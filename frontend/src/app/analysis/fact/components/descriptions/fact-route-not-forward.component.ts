import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'ui-fact-route-not-forward',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-not-forward">
      There is no path in the forward direction (from start node to end node).
    </p>
  `,
})
export class FactRouteNotForwardComponent {}
