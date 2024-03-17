import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-incomplete-ok',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-incomplete-ok">
      The route is marked as having an incomplete definition. A route definition is explicitely
      marked incomplete by adding a tag *"fixme"* with value *"incomplete"* in the route relation.
      But after analysis, the route seems to be ok.
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteIncompleteOkComponent {}
