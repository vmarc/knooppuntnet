import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-proposed-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-term
      level="info"
      name="Proposed"
      i18n-name="@@proposed-indicator.blue.title"
      i18n="@@node-connection-indicator.blue.text"
    >
      This node is _"proposed"_. <br /><br />The node has lifecycle prefix "proposed:" in the tag
      that makes it a network node, or has has tag _"state=proposed"_. The node is assumed to still
      be in a planning phase and likely not signposted in the field.
    </ui-term>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [TermComponent],
})
export class ProposedIndicatorComponent {}
