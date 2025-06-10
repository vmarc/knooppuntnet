import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-route-tag-missing',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-tag-missing">
      Routerelation does not contain the required _route_ tag.
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactRouteTagMissingComponent {}
