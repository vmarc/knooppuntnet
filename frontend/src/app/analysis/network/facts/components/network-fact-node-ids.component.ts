import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconNodeComponent } from '@app/components/shared/icon';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';

@Component({
  selector: 'kpn-network-fact-node-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (nodeId of nodeIds(); track nodeId) {
      <div class="kpn-align-center">
        <kpn-icon-node />
        <kpn-action-button-node [nodeId]="nodeId" />
        {{ nodeId }}
      </div>
    }
  `,
  standalone: true,
  imports: [ActionButtonNodeComponent, IconNodeComponent],
})
export class NetworkFactNodeIdsComponent {
  nodeIds = input.required<number[]>();
}
