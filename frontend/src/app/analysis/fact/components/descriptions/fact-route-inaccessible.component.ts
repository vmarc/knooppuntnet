import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-inaccessible',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-inaccessible">
      Part of the route does not seem
      [accessible](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#accessible).
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteInaccessibleComponent {}
