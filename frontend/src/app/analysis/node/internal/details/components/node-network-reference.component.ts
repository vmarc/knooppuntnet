import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { Reference } from '@api/common/common/reference';
import { IconNetworkLinkComponent } from '@app/shared/components/link/icon-network-link.component';

@Component({
  selector: 'kpn-node-network-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <kpn-icon-network-link [reference]="reference()" [mixedRouteScopes]="mixedRouteScopes()" />
    </div>
  `,
  imports: [IconNetworkLinkComponent],
})
export class NodeNetworkReferenceComponent {
  nodeInfo = input.required<NodeInfo>();
  reference = input.required<Reference>();
  mixedRouteScopes = input.required<boolean>();
}
