import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-route-inaccessible',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-inaccessible">
      Part of the route does not seem
      [accessible](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#accessible).
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactRouteInaccessibleComponent {}
