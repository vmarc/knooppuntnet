import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-route-node-name-mismatch',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-node-name-mismatch">
      The route name derived from the tags in the route relation does not match the expected name as
      derived from the start and end node of the route.
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactRouteNodeNameMismatchComponent {}
