import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-unexpected-integrity-check',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.unexpected-integrity-check">
      The node has an integrity check tag (_"expected_XXX_route_relations"_) where the _"XXX"_ does
      not match the _"XXX"_ in the tag key that contains the node name (_"XXX_ref"_, _"XXX_name"_,
      etc).
    </markdown>
  `,
  imports: [MarkdownModule],
})
export class FactUnexpectedIntegrityCheckComponent {}
