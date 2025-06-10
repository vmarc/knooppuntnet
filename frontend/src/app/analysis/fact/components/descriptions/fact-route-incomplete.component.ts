import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-route-incomplete',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-incomplete">
      The route is marked as having an incomplete definition. A route definition is explicitely
      marked incomplete by adding a tag *"fixme"* with value *"incomplete"* in the route relation.
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactRouteIncompleteComponent {}
