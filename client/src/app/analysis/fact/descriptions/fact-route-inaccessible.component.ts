import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-inaccessible',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-inaccessible">
      Part of the route does not seem
      [accessible](/en/docs/en.html#_accessible).
    </markdown>
  `,
})
export class FactRouteInaccessibleComponent {}
