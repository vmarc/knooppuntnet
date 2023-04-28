import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-node-name-mismatch',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-node-name-mismatch">
      The route name derived from the tags in the route relation does not match
      the expected name as derived from the start and end node of the route.
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteNodeNameMismatchComponent {}
