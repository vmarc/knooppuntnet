import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { IconNodeComponent } from '@app/shared/components/icon/icon-node.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';

@Component({
  selector: 'kpn-network-fact-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (node of nodes(); track node.id) {
      <div class="kpn-align-center">
        <kpn-icon-node />
        <kpn-action-button-node [nodeId]="node.id" />
        <kpn-link-node [nodeId]="node.id" [nodeName]="node.name" />
      </div>
    }
  `,
  imports: [LinkNodeComponent, IconNodeComponent, ActionButtonNodeComponent],
})
export class NetworkFactNodesComponent {
  nodes = input.required<Ref[]>();
}
