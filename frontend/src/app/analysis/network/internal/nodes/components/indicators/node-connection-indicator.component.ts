import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-node-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (node().detail.connection) {
      <div>
        <ui-term
          level="info"
          title="Connection"
          i18n-title="@@node-connection-indicator.blue.title"
          i18n="@@node-connection-indicator.blue.text"
        >
          This node is a connection to another network. All routes to this node have the role
          *"connection"* in the network relation.
        </ui-term>
      </div>
    }
  `,
  imports: [TermComponent],
})
export class NodeConnectionIndicatorComponent {
  readonly node = input.required<NetworkNodeRow>();
  readonly connection = computed(() => this.node().detail.connection);
}
