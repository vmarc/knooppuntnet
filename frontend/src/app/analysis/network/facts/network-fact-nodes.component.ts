import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Ref } from '@api/common/common';
import { LinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span *ngIf="nodes.length === 1" class="title kpn-label" i18n="@@network-facts.node">Node</span>
    <span *ngIf="nodes.length > 1" class="title kpn-label" i18n="@@network-facts.nodes">Nodes</span>
    <div class="kpn-comma-list">
      <kpn-link-node *ngFor="let node of nodes" [nodeId]="node.id" [nodeName]="node.name" />
    </div>
  `,
  standalone: true,
  imports: [NgIf, NgFor, LinkNodeComponent],
})
export class NetworkFactNodesComponent {
  @Input() nodes: Ref[];
}
