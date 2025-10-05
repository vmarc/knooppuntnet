import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-proposed-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (proposed()) {
      <div>
        <ui-term
          level="info"
          title="Proposed"
          i18n-title="@@proposed-indicator.blue.title"
          i18n="@@node-connection-indicator.blue.text"
        >
          This node is _"proposed"_. <br /><br />The node has lifecycle prefix "proposed:" in the
          tag that makes it a network node, or has has tag _"state=proposed"_. The node is assumed
          to still be in a planning phase and likely not signposted in the field.
        </ui-term>
      </div>
    }
  `,
  imports: [TermComponent],
})
export class ProposedIndicatorComponent {
  readonly node = input.required<NetworkNodeRow>();
  readonly proposed = computed(() => this.node().detail.proposed);
}
