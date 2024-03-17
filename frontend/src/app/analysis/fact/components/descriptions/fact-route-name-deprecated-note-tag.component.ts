import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-deprected-note-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-deprected-note-tag">
      The route name is defined in the _"note"_ tag. This is OK, but the use of the _"note"_ tag for
      route names is no longer recommended (deprecated). The idea is that the _"note"_ should be
      used for mapper notes only. The _"ref"_ and _"name"_ tags can be used for naming routes.
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteNameDeprecatedNoteTagComponent {}
