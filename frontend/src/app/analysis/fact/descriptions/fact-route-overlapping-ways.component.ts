import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-overlapping-ways',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-overlapping-ways">
      No detailed route analysis is performed because the route contains overlapping ways.
    </p>
  `,
  standalone: true,
})
export class FactRouteOverlappingWaysComponent {}
