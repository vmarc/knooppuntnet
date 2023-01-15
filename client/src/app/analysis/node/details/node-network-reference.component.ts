import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common/reference';
import { NodeInfo } from '@api/common/node-info';

@Component({
  selector: 'kpn-node-network-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <kpn-icon-network-link
        [reference]="reference"
        [mixedNetworkScopes]="mixedNetworkScopes"
      />
    </div>
  `,
})
export class NodeNetworkReferenceComponent {
  @Input() nodeInfo: NodeInfo;
  @Input() reference: Reference;
  @Input() mixedNetworkScopes: boolean;
}
