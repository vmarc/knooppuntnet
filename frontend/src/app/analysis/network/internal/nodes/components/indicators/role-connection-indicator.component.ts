import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-role-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (roleConnection()) {
      <div>
        <ui-term
          level="info"
          title="Connection"
          i18n-title="@@role-connection-indicator.blue.title"
          i18n="@@role-connection-indicator.blue.text"
        >
          This node is a connection to another network. This node has role *"connection"* in the
          network relation.
        </ui-term>
      </div>
    }
  `,
  imports: [TermComponent],
})
export class RoleConnectionIndicatorComponent {
  readonly node = input.required<NetworkNodeRow>();
  readonly roleConnection = computed(() => this.node().detail.roleConnection);
}
