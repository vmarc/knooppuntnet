import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-unused-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-unused-segments">
      The route contains ways or part of ways that are not used in the
      connection between the end nodes.
    </p>
  `,
})
export class FactRouteUnusedSegmentsComponent {}
