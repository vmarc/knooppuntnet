import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-name-missing',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-name-missing">
      Could not determine the route name. The route relation does not have a
      _"ref"_, _"name"_ or _"note"_ tag with the route name, and also no
      _"from"_ and _"to"_ tags from which the route name can be derived.
    </markdown>
  `,
})
export class FactRouteNameMissingComponent {}
