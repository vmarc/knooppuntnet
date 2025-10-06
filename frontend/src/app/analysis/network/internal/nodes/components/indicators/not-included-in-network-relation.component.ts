import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-not-included-in-network-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-term
      level="error"
      name="Not included in network relation"
      i18n-name="@@network-indicator.red.title"
      i18n="@@network-indicator.red.text"
    >
      This node is not included as a member in the network relation. This is not OK.
      <br /><br />The convention is to include each node in the network relation.<br /><br />
      An exception is when the node belongs to another network (all routes to this node have role
      *"connection"* in the network relation), then the node does not have to be included as member
      in the network relation. The node can be added the network relation, but should get the role
      *"connection"* in that case.
    </ui-term>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [TermComponent],
})
export class NotIncludedInNetworkRelationComponent {}
