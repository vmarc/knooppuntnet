import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-node-missing-in-ways',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-node-missing-in-ways">
      One or more of the nodes is not found in any of the ways of this route.
    </p>
  `,
})
export class FactRouteNodeMissingInWaysComponent {}
