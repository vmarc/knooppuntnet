import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-node-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-term
      level="info"
      name="Connection"
      i18n-name="@@node-connection-indicator.blue.title"
      i18n="@@node-connection-indicator.blue.text"
    >
      This node is a connection to another network. All routes to this node have the role
      *"connection"* in the network relation.
    </ui-term>
  `,
  imports: [TermComponent],
})
export class NodeConnectionIndicatorComponent {}
