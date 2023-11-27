import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-lost-route-tags',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.lost-route-tags">
      This relation is no longer a valid network route because a required tag has been removed.
    </p>
  `,
  standalone: true,
})
export class FactLostRouteTagsComponent {}
