import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconNodeComponent } from '@app/components/shared/icon';
import { IconRouteComponent } from '@app/components/shared/icon';
import { LinkRouteComponent } from '@app/components/shared/link';
import { JosmNodeComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { ActionButtonNodeComponent } from '../../components/action/action-button-node.component';
import { ActionButtonRouteComponent } from '../../components/action/action-button-route.component';

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
  imports: [
    OsmLinkNodeComponent,
    JosmNodeComponent,
    IconRouteComponent,
    ActionButtonRouteComponent,
    LinkRouteComponent,
    ActionButtonNodeComponent,
    IconNodeComponent,
  ],
})
export class NetworkFactNodeIdsComponent {
  nodeIds = input.required<number[]>();
}
