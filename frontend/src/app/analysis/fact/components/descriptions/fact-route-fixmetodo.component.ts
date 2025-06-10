import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-route-fixmetodo',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-fixmetodo">
      Route definition needs work (has tag *"fixmetodo"*).
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactRouteFixmetodoComponent {}
