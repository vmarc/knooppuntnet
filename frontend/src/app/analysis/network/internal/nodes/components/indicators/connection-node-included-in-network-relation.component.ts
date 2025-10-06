import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-connection-node-included-in-network-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-term
      level="error"
      name="Connection node included in network relation"
      i18n-name="@@network-indicator.gray.title"
      i18n="@@network-indicator.orange.text"
    >
      This node is included as a member in the network relation.<br /><br />
      We did not expect this, because all routes to this node have role *"connection"*. This would
      mean that the node is part of another network. We expect that the node is not included in the
      network relation, unless it receives the role *"connection"*.
    </ui-term>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [TermComponent],
})
export class ConnectionNodeIncludedInNetworkRelationComponent {}
