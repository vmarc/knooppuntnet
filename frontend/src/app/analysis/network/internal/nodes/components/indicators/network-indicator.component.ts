import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-network-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (connectionNodeIncludedInNetworkRelation()) {
      <div>
        <ui-term
          level="error"
          title="Connection node included in network relation"
          i18n-title="@@network-indicator.gray.title"
          i18n="@@network-indicator.orange.text"
        >
          This node is included as a member in the network relation.<br /><br />
          We did not expect this, because all routes to this node have role *"connection"*. This
          would mean that the node is part of another network. We expect that the node is not
          included in the network relation, unless it receives the role *"connection"*.
        </ui-term>
      </div>
    } @else if (notIncludedInNetworkRelation()) {
      <div>
        <ui-term
          level="error"
          title="Not included in network relation"
          i18n-title="@@network-indicator.red.title"
          i18n="@@network-indicator.red.text"
        >
          This node is not included as a member in the network relation. This is not OK.
          <br /><br />The convention is to include each node in the network relation.<br /><br />
          An exception is when the node belongs to another network (all routes to this node have
          role *"connection"* in the network relation), then the node does not have to be included
          as member in the network relation. The node can be added the network relation, but should
          get the role *"connection"* in that case.
        </ui-term>
      </div>
    }
  `,
  imports: [TermComponent],
})
export class NetworkIndicatorComponent {
  readonly node = input.required<NetworkNodeRow>();

  readonly notIncludedInNetworkRelation = computed(
    () => !this.node().detail.definedInRelation && !this.node().detail.connection
  );

  readonly connectionNodeIncludedInNetworkRelation = computed(
    () =>
      this.node().detail.definedInRelation &&
      this.node().detail.connection &&
      !this.node().detail.roleConnection
  );
}
