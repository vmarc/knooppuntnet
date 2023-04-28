import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-incomplete',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-incomplete">
      The route is marked as having an incomplete definition. A route definition
      is explicitely marked incomplete by adding a tag *"fixme"* with value
      *"incomplete"* in the route relation.
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteIncompleteComponent {}
