import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-without-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-without-nodes">
      The route does not contain any network nodes (we expect the route to
      contain at least 2 network nodes).
    </p>
  `,
})
export class FactRouteWithoutNodesComponent {}
