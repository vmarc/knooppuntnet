import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { LinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (nodes().length === 1) {
      <span class="title kpn-label" i18n="@@network-facts.node">Node</span>
    } @else if (nodes().length > 1) {
      <span class="title kpn-label" i18n="@@network-facts.nodes">Nodes</span>
    }
    <div class="kpn-comma-list">
      @for (node of nodes(); track node.id) {
        <kpn-link-node [nodeId]="node.id" [nodeName]="node.name" />
      }
    </div>
  `,
  standalone: true,
  imports: [LinkNodeComponent],
})
export class NetworkFactNodesComponent {
  nodes = input<Ref[] | undefined>();
}
