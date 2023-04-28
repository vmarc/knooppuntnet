import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-fixmetodo',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-fixmetodo">
      Route definition needs work (has tag *"fixmetodo"*).
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteFixmetodoComponent {}
