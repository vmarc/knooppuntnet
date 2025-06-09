import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconNodeComponent } from '@app/shared/components/icon/icon-node.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';

@Component({
  selector: 'ui-network-fact-node-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (nodeId of nodeIds(); track nodeId) {
      <div class="kpn-align-center">
        <ui-icon-node />
        <ui-action-button-node [nodeId]="nodeId" />
        {{ nodeId }}
      </div>
    }
  `,
  imports: [ActionButtonNodeComponent, IconNodeComponent],
})
export class NetworkFactNodeIdsComponent {
  readonly nodeIds = input.required<number[]>();
}
